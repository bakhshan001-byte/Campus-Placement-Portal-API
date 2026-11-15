
package com.muhammadakram.campus_placement_portal.controller;

import com.muhammadakram.campus_placement_portal.entity.Job;
import com.muhammadakram.campus_placement_portal.repository.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    // Publish Job
    @PostMapping("/post")
    public Job postJob(@RequestBody Job job) {

        return jobRepository.save(job);
    }

    // Get All Jobs
    @GetMapping("/all")
    public List<Job> getAllJobs() {

        return jobRepository.findAll();
    }

    // Delete Job
    @DeleteMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {

        if (jobRepository.existsById(id)) {

            jobRepository.deleteById(id);

            return "Job deleted successfully";

        } else {

            return "Job not found";
        }
    }
}
