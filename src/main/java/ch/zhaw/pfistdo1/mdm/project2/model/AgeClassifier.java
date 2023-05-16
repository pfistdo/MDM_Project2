package ch.zhaw.pfistdo1.mdm.project2.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Collections;

import javax.imageio.ImageIO;

import ai.onnxruntime.*;

/**
 * AgeClassifier is a class that performs age classification using an ONNX
 * model.
 */
public class AgeClassifier {

    // Array of class labels
    private static final String[] LABELS = { "0-2", "3-9", "10-19", "20-29", "30-39", "40-49", "50-59", "60-69",
            "more than 70" };

    /**
     * The main entry point of the AgeClassifier application.
     *
     * @param args command line arguments
     * @throws IOException  if there is an error reading the image file
     * @throws OrtException if there is an error with the ONNX model or inference
     */
    public static void main(String[] args) throws IOException, OrtException {

        // Load the ONNX model
        OrtEnvironment env = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
        OrtSession session = env.createSession("src/main/resources/static/model/vit_age_classifier.onnx", opts);

        // Load and preprocess the image
        BufferedImage image = ImageIO.read(new File("src/main/resources/static/images/face.jpg"));
        float[] input = preProcessImage(image);

        // Reshape the input tensor to have a rank of 4
        long[] inputShape = { 1, 3, 224, 224 };
        FloatBuffer floatBuffer = FloatBuffer.wrap(input);
        OnnxTensor inputTensor = OnnxTensor.createTensor(env, floatBuffer, inputShape);

        // Run inference on the image
        OrtSession.Result output = session.run(Collections.singletonMap("input", inputTensor));
        float[][] outputArray = (float[][]) output.get(0).getValue();

        // Get the predicted class
        int predictedClass = argmax(outputArray[0]);
        System.out.println("Predicted class: " + predictedClass);
        System.out.println("Predicted label: " + getPredictedClassLabel(predictedClass));
    }

    /**
     * Preprocesses the input image by resizing it and converting it to a float
     * array.
     *
     * @param image the input image to preprocess
     * @return the preprocessed float array
     */
    private static float[] preProcessImage(BufferedImage image) {
        // Resize the image to the expected dimensions
        int targetWidth = 224;
        int targetHeight = 224;
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, targetWidth, targetHeight, null);
        g.dispose();

        // Convert the resized image to pixel array
        int[] pixels = resizedImage.getRGB(0, 0, targetWidth, targetHeight, null, 0, targetWidth);

        // Perform normalization and create the float array
        float[] image_np = new float[3 * targetWidth * targetHeight];
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            image_np[i * 3] = ((pixel >> 16) & 0xFF) / 255.0f;
            image_np[i * 3 + 1] = ((pixel >> 8) & 0xFF) / 255.0f;
            image_np[i * 3 + 2] = (pixel & 0xFF) / 255.0f;
        }
        return image_np;
    }

    /**
     * Gets the predicted class label based on the predicted class index.
     *
     * @param predictedClass the predicted class index
     * @return the corresponding class label
     */
    private static String getPredictedClassLabel(int predictedClass) {
        if (predictedClass >= 0 && predictedClass < LABELS.length) {
            return LABELS[predictedClass];
        }
        return "Unknown";
    }

    /**
     * Finds the index of the maximum value in the array.
     *
     * @param array the input array
     * @return the index of the maximum value
     */
    private static int argmax(float[] array) {
        int maxIndex = 0;
        float max = array[maxIndex];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}