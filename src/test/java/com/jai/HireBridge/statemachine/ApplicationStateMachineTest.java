package com.jai.HireBridge.statemachine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.jai.HireBridge.model.AppliStatus;

public class ApplicationStateMachineTest 
{
   private ApplicationStateMachine machine;
   
   @BeforeEach
   void setUp() {
	   machine = new ApplicationStateMachine();
   }
	
   @ParameterizedTest
   @CsvSource({
	   "APPLIED, SHORTLISTED",
	   "APPLIED, REJECTED",
	   "SHORTLISTED, SELECTED",
	   "SHORTLISTED, SELECTED"
   })
   void allowsValidTransitions(AppliStatus from,AppliStatus to) {
	   assertTrue(machine.isValidTransition(from, to));
   }
   
   @ParameterizedTest
   @CsvSource({
       "APPLIED, APPLIED",
       "APPLIED, SELECTED",
       "SHORTLISTED, APPLIED",
       "SHORTLISTED, SHORTLISTED",
       "SELECTED, APPLIED",
       "SELECTED, SHORTLISTED",
       "SELECTED, REJECTED",
       "REJECTED, APPLIED",
       "REJECTED, SHORTLISTED",
       "REJECTED, SELECTED"
   })
   void blocksInvalidTransitions(AppliStatus from, AppliStatus to) {
       assertFalse(machine.isValidTransition(from, to));
   }

   @Test
   void finalStatesHaveNoNextStates() {
       assertTrue(machine.allowedNext(AppliStatus.SELECTED).isEmpty());
       assertTrue(machine.allowedNext(AppliStatus.REJECTED).isEmpty());
   }

   @Test
   void nullStatusIsNeverValid() {
       assertFalse(machine.isValidTransition(null, AppliStatus.SHORTLISTED));
       assertFalse(machine.isValidTransition(AppliStatus.APPLIED, null));
   }
}
