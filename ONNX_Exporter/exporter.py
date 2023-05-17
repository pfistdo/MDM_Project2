import torch
from transformers import ViTForImageClassification

# Load the ViT model and feature extractor
model_name = "nateraw/vit-age-classifier"
model = ViTForImageClassification.from_pretrained(model_name)

# Set the model to evaluation mode
model.eval()

# Create an example input tensor (adjust the shape according to your model's input requirements)
dummy_input = torch.randn(1, 3, 224, 224)

# Export the model to ONNX format
output_onnx = "ONNX_Exporter/vit_age_classifier.onnx"  # Replace with the desired output file name

# Provide an example input tensor to the model when exporting to ONNX
torch.onnx.export(model, dummy_input, output_onnx, opset_version=11, input_names=['input'], output_names=['output'], verbose=True)