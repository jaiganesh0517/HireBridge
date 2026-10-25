package com.jai.HireBridge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.jai.HireBridge.exception.BusinessRuleException;
import com.jai.HireBridge.exception.DuplicateResourceException;
import com.jai.HireBridge.exception.ResourceNotFoundException;
import com.jai.HireBridge.exception.UnauthorizedException;
import com.jai.HireBridge.model.AppliStatus;
import com.jai.HireBridge.model.Application;
import com.jai.HireBridge.model.JobPosting;
import com.jai.HireBridge.model.Status;
import com.jai.HireBridge.model.StudentProfile;
import com.jai.HireBridge.repositories.ApplicationRepository;
import com.jai.HireBridge.repositories.JobEligibleRepository;
import com.jai.HireBridge.repositories.JobPostingRepository;
import com.jai.HireBridge.repositories.StudentRepository;
import com.jai.HireBridge.repositories.UsersRepository;
import com.jai.HireBridge.statemachine.ApplicationStateMachine;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock private ApplicationRepository appRepo;
    @Mock private JobPostingRepository jbRepo;
    @Mock private StudentRepository sPRepo;
    @Mock private JobEligibleRepository jERepo;
    @Mock private UsersRepository userRepo;
    @Mock private ApplicationStateMachine appMachine;
    @Mock private ResumeStorageService resumeStorage;

    private ApplicationService service;

    private static final Long STUDENT_ID = 1L;
    private static final Long JOB_ID = 10L;
    private static final Long RECRUITER_ID = 1000L;
    private static final Long OTHER_RECRUITER_ID = 2000L;
    private static final Long APPLICATION_ID = 50L;

    private static final String DEFAULT_KEY = "resumes/1/default.pdf";
    private static final String COPY_KEY = "resumes/1/copy.pdf";

    @BeforeEach
    void setUp() {
        // VERIFY: argument order must match your real constructor
        service = new ApplicationService(appRepo, jbRepo, sPRepo, jERepo, userRepo, appMachine, resumeStorage);
    }

    // ---------- helpers ----------

    private Application existingApplication() {
        Application app = new Application();
        app.setStudentId(STUDENT_ID);
        app.setJobId(JOB_ID);
        app.setStatus(AppliStatus.APPLIED);
        return app;
    }

    private JobPosting jobOwnedBy(Long recruiterId) {
        JobPosting job = new JobPosting();
        job.setRecruiterId(recruiterId);
        return job;
    }

    private JobPosting openJob() {
        JobPosting job = new JobPosting();
        job.setJobStatus(Status.OPEN);
        job.setDeadline(LocalDateTime.now().plusDays(5));
        job.setMinCgpa(7.0);
        return job;
    }

    private StudentProfile profile(double cgpa, String branch) {
        StudentProfile p = new StudentProfile();
        p.setCgpa(cgpa);
        p.setBranch(branch);
        return p;
    }

    private StudentProfile profileWithResume(double cgpa, String branch) {
        StudentProfile p = profile(cgpa, branch);
        p.setResumeKey(DEFAULT_KEY);
        p.setResumeFileName("cv.pdf");
        p.setResumeContentType("application/pdf");
        p.setResumeSize(1000L);
        return p;
    }

    // ---------- applyToJob ----------

    @Test
    void applyToJob_jobNotFound_throwsNotFound() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID, null));
        verify(appRepo, never()).save(any());
    }

    @Test
    void applyToJob_jobClosed_throwsBusinessRule() {
        JobPosting job = openJob();
        job.setJobStatus(Status.CLOSED);
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(job));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID, null));
        assertEquals("This job is closed.", ex.getMessage());
        verify(appRepo, never()).save(any());
    }

    @Test
    void applyToJob_deadlinePassed_throwsBusinessRule() {
        JobPosting job = openJob();
        job.setDeadline(LocalDateTime.now().minusDays(1));
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(job));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID, null));
        assertEquals("Application deadline has passed for this job.", ex.getMessage());
        verify(appRepo, never()).save(any());
    }

    @Test
    void applyToJob_duplicateApplication_throwsDuplicate() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID, null));
        verify(appRepo, never()).save(any());
    }

    @Test
    void applyToJob_profileMissing_throwsNotFound() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(false);
        when(sPRepo.findById(STUDENT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID, null));
        verify(appRepo, never()).save(any());
    }

    @Test
    void applyToJob_cgpaTooLow_throwsBusinessRule() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob())); // min 7.0
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(false);
        when(sPRepo.findById(STUDENT_ID)).thenReturn(Optional.of(profileWithResume(6.5, "CSE")));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID, null));
        assertEquals("CGPA is lower than expected", ex.getMessage());
        verify(appRepo, never()).save(any());
        verifyNoInteractions(resumeStorage); // no S3 work for an ineligible student
    }

    @Test
    void applyToJob_branchNotEligible_throwsBusinessRule() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(false);
        when(sPRepo.findById(STUDENT_ID)).thenReturn(Optional.of(profileWithResume(8.0, "MECH")));
        when(jERepo.existsByJobIdAndBranch(JOB_ID, "MECH")).thenReturn(false);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID, null));
        assertEquals("Your branch is not eligible for this job", ex.getMessage());
        verify(appRepo, never()).save(any());
        verifyNoInteractions(resumeStorage); // no S3 work for an ineligible student
    }

    @Test
    void applyToJob_noFile_copiesDefaultResume() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(false);
        when(sPRepo.findById(STUDENT_ID)).thenReturn(Optional.of(profileWithResume(8.0, "CSE")));
        when(jERepo.existsByJobIdAndBranch(JOB_ID, "CSE")).thenReturn(true);
        when(resumeStorage.copy(DEFAULT_KEY, STUDENT_ID)).thenReturn(COPY_KEY);
        when(appRepo.save(any(Application.class))).thenAnswer(inv -> inv.getArgument(0));

        Application result = service.applyToJob(STUDENT_ID, JOB_ID, null);

        ArgumentCaptor<Application> captor = ArgumentCaptor.forClass(Application.class);
        verify(appRepo).save(captor.capture());
        Application saved = captor.getValue();
        System.out.println("SAVED: " + saved.getStudentId() + " | " + saved.getResumeKey());
        System.out.println("SAME OBJECT: " + (saved == result));
        assertEquals(STUDENT_ID, result.getStudentId());
        assertEquals(JOB_ID, result.getJobId());
        assertEquals(AppliStatus.APPLIED, result.getStatus());
        assertNotNull(result.getAppliedAt());
        assertEquals(COPY_KEY, result.getResumeKey()); // its own copy, not the profile's key
        assertEquals("cv.pdf", result.getResumeFileName());
        verify(resumeStorage, never()).upload(any(), any());
    }

    @Test
    void applyToJob_noFileAndNoDefaultResume_throwsBusinessRule() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(false);
        when(sPRepo.findById(STUDENT_ID)).thenReturn(Optional.of(profile(8.0, "CSE")));
        when(jERepo.existsByJobIdAndBranch(JOB_ID, "CSE")).thenReturn(true);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID, null));
        assertEquals("Please upload a resume", ex.getMessage());
        verify(appRepo, never()).save(any());
        verifyNoInteractions(resumeStorage);
    }

    @Test
    void applyToJob_withFile_uploadsAndSkipsCopy() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "job-specific.pdf", "application/pdf", "%PDF-1.4".getBytes());

        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(false);
        when(sPRepo.findById(STUDENT_ID)).thenReturn(Optional.of(profileWithResume(8.0, "CSE")));
        when(jERepo.existsByJobIdAndBranch(JOB_ID, "CSE")).thenReturn(true);
        when(resumeStorage.upload(eq(STUDENT_ID), any())).thenReturn("resumes/1/new.pdf");
        when(appRepo.save(any(Application.class))).thenAnswer(inv -> inv.getArgument(0));

        Application result = service.applyToJob(STUDENT_ID, JOB_ID, file);

        assertEquals("resumes/1/new.pdf", result.getResumeKey());
        assertEquals("job-specific.pdf", result.getResumeFileName());
        verify(resumeStorage, never()).copy(any(), any());
    }

    @Test
    void applyToJob_saveFails_deletesNewResume() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(false);
        when(sPRepo.findById(STUDENT_ID)).thenReturn(Optional.of(profileWithResume(8.0, "CSE")));
        when(jERepo.existsByJobIdAndBranch(JOB_ID, "CSE")).thenReturn(true);
        when(resumeStorage.copy(DEFAULT_KEY, STUDENT_ID)).thenReturn(COPY_KEY);
        when(appRepo.save(any(Application.class))).thenThrow(new RuntimeException("db down"));

        assertThrows(RuntimeException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID, null));

        verify(resumeStorage).delete(COPY_KEY); // the orphaned copy is cleaned up
    }
    @Test
    void getResumeUrl_applicationNotFound_throwsNotFound() {
        when(appRepo.findById(APPLICATION_ID)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.getResumeUrl(STUDENT_ID, APPLICATION_ID));
        verifyNoInteractions(resumeStorage);
    }

    @Test
    void getResumeUrl_stranger_throwsUnauthorized() {
        Application app = existingApplication();
        app.setResumeKey("resumes/1/x.pdf");
        when(appRepo.findById(APPLICATION_ID)).thenReturn(Optional.of(app));
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(jobOwnedBy(RECRUITER_ID)));

        assertThrows(UnauthorizedException.class,
                () -> service.getResumeUrl(OTHER_RECRUITER_ID, APPLICATION_ID));
        verifyNoInteractions(resumeStorage);
    }

    @Test
    void getResumeUrl_ownerStudent_returnsUrl() {
        Application app = existingApplication();
        app.setResumeKey("resumes/1/x.pdf");
        app.setResumeFileName("cv.pdf");
        when(appRepo.findById(APPLICATION_ID)).thenReturn(Optional.of(app));
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(jobOwnedBy(RECRUITER_ID)));
        when(resumeStorage.presignedUrl("resumes/1/x.pdf", "cv.pdf")).thenReturn("https://signed");

        assertEquals("https://signed", service.getResumeUrl(STUDENT_ID, APPLICATION_ID));
    }

    @Test
    void getResumeUrl_jobRecruiter_returnsUrl() {
        Application app = existingApplication();
        app.setResumeKey("resumes/1/x.pdf");
        app.setResumeFileName("cv.pdf");
        when(appRepo.findById(APPLICATION_ID)).thenReturn(Optional.of(app));
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(jobOwnedBy(RECRUITER_ID)));
        when(resumeStorage.presignedUrl("resumes/1/x.pdf", "cv.pdf")).thenReturn("https://signed");

        assertEquals("https://signed", service.getResumeUrl(RECRUITER_ID, APPLICATION_ID));
    }

    @Test
    void getResumeUrl_noResumeOnApplication_throwsNotFound() {
        Application app = existingApplication(); // resumeKey is null
        when(appRepo.findById(APPLICATION_ID)).thenReturn(Optional.of(app));
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(jobOwnedBy(RECRUITER_ID)));

        assertThrows(ResourceNotFoundException.class,
                () -> service.getResumeUrl(STUDENT_ID, APPLICATION_ID));
        verifyNoInteractions(resumeStorage);
    }

    // ---------- updateApplicationStatus ----------

    @Test
    void updateStatus_applicationNotFound_throwsNotFound() {
        when(appRepo.findById(APPLICATION_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateApplicationStatus(RECRUITER_ID, APPLICATION_ID, AppliStatus.SHORTLISTED));
        verify(appRepo, never()).save(any());
    }

    @Test
    void updateStatus_jobNotFound_throwsNotFound() {
        when(appRepo.findById(APPLICATION_ID)).thenReturn(Optional.of(existingApplication()));
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateApplicationStatus(RECRUITER_ID, APPLICATION_ID, AppliStatus.SHORTLISTED));
        verify(appRepo, never()).save(any());
    }

    @Test
    void updateStatus_recruiterDoesNotOwnJob_throwsUnauthorized() {
        Application app = existingApplication();
        when(appRepo.findById(APPLICATION_ID)).thenReturn(Optional.of(app));
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(jobOwnedBy(RECRUITER_ID)));

        assertThrows(UnauthorizedException.class,
                () -> service.updateApplicationStatus(OTHER_RECRUITER_ID, APPLICATION_ID, AppliStatus.SELECTED));

        assertEquals(AppliStatus.APPLIED, app.getStatus()); // status must be untouched
        verify(appRepo, never()).save(any());
    }

    @Test
    void updateStatus_invalidTransition_throwsBusinessRule() {
        Application app = existingApplication();
        when(appRepo.findById(APPLICATION_ID)).thenReturn(Optional.of(app));
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(jobOwnedBy(RECRUITER_ID)));
        when(appMachine.isValidTransition(AppliStatus.APPLIED, AppliStatus.SELECTED)).thenReturn(false);

        assertThrows(BusinessRuleException.class,
                () -> service.updateApplicationStatus(RECRUITER_ID, APPLICATION_ID, AppliStatus.SELECTED));

        assertEquals(AppliStatus.APPLIED, app.getStatus());
        verify(appRepo, never()).save(any());
    }

    @Test
    void updateStatus_ownerRecruiter_updatesAndSaves() {
        Application app = existingApplication();
        when(appRepo.findById(APPLICATION_ID)).thenReturn(Optional.of(app));
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(jobOwnedBy(RECRUITER_ID)));
        when(appMachine.isValidTransition(AppliStatus.APPLIED, AppliStatus.SHORTLISTED)).thenReturn(true);

        Application result = service.updateApplicationStatus(
                RECRUITER_ID, APPLICATION_ID, AppliStatus.SHORTLISTED);

        assertEquals(AppliStatus.SHORTLISTED, result.getStatus());
        verify(appRepo).save(app);
    }
}