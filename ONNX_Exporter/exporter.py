import PIL
from transformers import ViTForImageClassification, ViTImageProcessor
import torch

# Load the model and the feature extractor
model = ViTForImageClassification.from_pretrained('nateraw/vit-age-classifier')
feature_extractor = ViTImageProcessor.from_pretrained('nateraw/vit-age-classifier')

# Export the model to ONNX format
image = PIL.Image.open('ONNX_Exporter/face.jpg')
inputs = feature_extractor(image, return_tensors='pt')

torch.onnx.export(model, inputs['pixel_values'], 'ONNX_Exporter/vit_age_classifier.onnx', input_names=['input'], output_names=['output'], opset_version=13)
