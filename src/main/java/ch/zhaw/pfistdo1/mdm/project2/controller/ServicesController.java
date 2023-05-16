package ch.zhaw.pfistdo1.mdm.project2.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ai.onnxruntime.OrtException;
import ch.zhaw.pfistdo1.mdm.project2.model.AgeClassifier;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ServicesController {

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("templates/index.html");
    }

    @GetMapping("/ping")
    public String ping() {
        return "Sentiment app is up and running!";
    }

    @PostMapping("/ageClassifier")
    public String predict(@RequestParam("image") MultipartFile imageFile) {
        try {
            BufferedImage image = ImageIO.read(imageFile.getInputStream());
            return AgeClassifier.getAge(image);
        } catch (IOException | OrtException e) {
            e.printStackTrace();
            return "Error processing the image.";
        }
    }

}