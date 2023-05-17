package ch.zhaw.pfistdo1.mdm.project2.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Collections;

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
     * Performs age classification on the given image and returns the predicted age.
     *
     * @param image the input image to perform age classification on
     * @return the predicted age as a string
     * @throws IOException  if there is an error reading the image file
     * @throws OrtException if there is an error with the ONNX model or inference
     */
    public static String getAge(BufferedImage image) throws IOException, OrtException {
        // Load the ONNX model
        OrtEnvironment env = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
        OrtSession session = env.createSession("src/main/resources/static/model/vit_age_classifier.onnx", opts);

        // Load and preprocess the image
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
        return getPredictedClassLabel(predictedClass);
    }

    /**
     * Preprocesses the input image by resizing and normalizing it to a float array.
     * Saves the processed image.
     *
     * @param image the input image to preprocess
     * @return the preprocessed float array
     */
    private static float[] preProcessImage(BufferedImage image) throws IOException {
        // Resize the image to the desired dimensions
        int targetWidth = 224;
        int targetHeight = 224;
        BufferedImage resizedImage = resizeImage(image, targetWidth, targetHeight);

        // Normalize the pixel values to the range of [0, 1]
        float[] image_np = new float[3 * targetWidth * targetHeight];
        int[] pixels = resizedImage.getRGB(0, 0, targetWidth, targetHeight, null, 0, targetWidth);
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            image_np[i * 3] = ((pixel >> 16) & 0xFF) / 255.0f;
            image_np[i * 3 + 1] = ((pixel >> 8) & 0xFF) / 255.0f;
            image_np[i * 3 + 2] = (pixel & 0xFF) / 255.0f;
        }

        return image_np;
    }

    /**
     * Resizes the input image to the target dimensions.
     *
     * @param image        the input image
     * @param targetWidth  the target width
     * @param targetHeight the target height
     * @return the resized image
     */
    private static BufferedImage resizeImage(BufferedImage image, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage;
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