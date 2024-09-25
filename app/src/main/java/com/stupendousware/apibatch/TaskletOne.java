package com.stupendousware.apibatch;

import javax.annotation.Nullable;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskletOne implements Tasklet {

    @Override
    @Nullable
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Executing a tasklet");
        return RepeatStatus.FINISHED;
    }
}
