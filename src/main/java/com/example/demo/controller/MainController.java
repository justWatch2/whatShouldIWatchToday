package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class MainController {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @GetMapping("/first")
    public String first(@RequestParam String value) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("data", value)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("firstJob"), jobParameters);

        return "ok";
    }
    @GetMapping("/second")
    public String second() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("data", "second")
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("firstJob"), jobParameters);

        return "ok";
    }
    @GetMapping("/third")
    public String third() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("data", "first")
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("keyJob"), jobParameters);

        return "ok";
    }
}
