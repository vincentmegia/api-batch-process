package com.stupendousware.apibatch.controllers;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Logger;

@RequestMapping("/batch")
@RestController
public class BatchController {
    private static final Logger logger = Logger.getLogger(BatchController.class.getName());

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @GetMapping("/adhoc/start")
    public ResponseEntity<?> start() {
        logger.info("batch is starting");
        logger.info("job: " + job);
        try {
            var jobExecution = jobLauncher.run(job, new JobParameters());
            logger.info("Job ended: " + jobExecution.getEndTime());
            var response = ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
            return response;
        } catch (Exception e) {
            logger.info("an error has occured: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "an error has occured");
        }
    }
}
