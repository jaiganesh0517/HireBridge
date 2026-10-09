package com.jai.HireBridge.service;
import com.jai.HireBridge.exception.UnauthorizedException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jai.HireBridge.exception.BusinessRuleException;
import com.jai.HireBridge.exception.DuplicateResourceException;
import com.jai.HireBridge.exception.ResourceNotFoundException;
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

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock private ApplicationRepository appRepo;
    @Mock private JobPostingRepository jbRepo;
    @Mock private StudentRepository sPRepo;
    @Mock private JobEligibleRepository jERepo;
    @Mock private UsersRepository userRepo;

    private ApplicationService service;

    private static final Long STUDENT_ID = 1L;
    private static final Long JOB_ID = 10L;
    private static final Long RECRUITER_ID = 1000L;   // owner of the job
    private static final Long OTHER_RECRUITER_ID = 2000L;
    private static final Long APPLICATION_ID = 50L;

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

    @BeforeEach
    void setUp() {
        service = new ApplicationService(appRepo, jbRepo, sPRepo, jERepo, userRepo);
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

    

    @Test
    void applyToJob_jobNotFound_throwsNotFound() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID));
        verify(appRepo, never()).save(any());
    }

    @Test
    void applyToJob_jobClosed_throwsBusinessRule() {
        JobPosting job = openJob();
        job.setJobStatus(Status.CLOSED);
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(job));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID));
        assertEquals("This job is closed.", ex.getMessage());
        verify(appRepo, never()).save(any());
    }

    @Test
    void applyToJob_deadlinePassed_throwsBusinessRule() {
        JobPosting job = openJob();
        job.setDeadline(LocalDateTime.now().minusDays(1));
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(job));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID));
        assertEquals("Application deadline has passed for this job.", ex.getMessage());
        verify(appRepo, never()).save(any());
    }

    @Test
    void applyToJob_duplicateApplication_throwsDuplicate() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID));
        verify(appRepo, never()).save(any());
    }

    @Test
    void applyToJob_profileMissing_throwsNotFound() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(false);
        when(sPRepo.findById(STUDENT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID));
        verify(appRepo, never()).save(any());
    }

    @Test
    void applyToJob_cgpaTooLow_throwsBusinessRule() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob())); // min 7.0
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(false);
        when(sPRepo.findById(STUDENT_ID)).thenReturn(Optional.of(profile(6.5, "CSE")));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID));
        assertEquals("CGPA is lower than expected", ex.getMessage());
        verify(appRepo, never()).save(any());
    }

    @Test
    void applyToJob_branchNotEligible_throwsBusinessRule() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(false);
        when(sPRepo.findById(STUDENT_ID)).thenReturn(Optional.of(profile(8.0, "MECH")));
        when(jERepo.existsByJobIdAndBranch(JOB_ID, "MECH")).thenReturn(false);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.applyToJob(STUDENT_ID, JOB_ID));
        assertEquals("Your branch is not eligible for this job", ex.getMessage());
        verify(appRepo, never()).save(any());
    }

    @Test
    void applyToJob_allRulesPass_savesApplication() {
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByStudentIdAndJobId(STUDENT_ID, JOB_ID)).thenReturn(false);
        when(sPRepo.findById(STUDENT_ID)).thenReturn(Optional.of(profile(8.0, "CSE")));
        when(jERepo.existsByJobIdAndBranch(JOB_ID, "CSE")).thenReturn(true);
        when(appRepo.save(any(Application.class))).thenAnswer(inv -> inv.getArgument(0));

        Application result = service.applyToJob(STUDENT_ID, JOB_ID);

        assertEquals(STUDENT_ID, result.getStudentId());
        assertEquals(JOB_ID, result.getJobId());
        assertEquals(AppliStatus.APPLIED, result.getStatus());
        assertNotNull(result.getAppliedAt());
        verify(appRepo).save(any(Application.class));
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
    void updateStatus_ownerRecruiter_updatesAndSaves() {
        Application app = existingApplication();
        when(appRepo.findById(APPLICATION_ID)).thenReturn(Optional.of(app));
        when(jbRepo.findById(JOB_ID)).thenReturn(Optional.of(jobOwnedBy(RECRUITER_ID)));

        Application result = service.updateApplicationStatus(
                RECRUITER_ID, APPLICATION_ID, AppliStatus.SHORTLISTED);

        assertEquals(AppliStatus.SHORTLISTED, result.getStatus());
        verify(appRepo).save(app);
    }
}