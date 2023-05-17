function predict_age() {
  var img = document.getElementById("faceImage").files[0];
  var btn = document.getElementById("predictBtn");
  var resultDiv = document.getElementById("result");

  if (img != undefined) {
    // Prepare form data
    var form_data = new FormData();
    form_data.append("image", img);

    // Hide button text and show spinner
    btn.innerHTML = '<span class="spinner-border spinner-border-sm"></span>';

    $.ajax({
      url: "ageClassifier",
      dataType: "text",
      cache: false,
      contentType: false,
      processData: false,
      data: form_data,
      type: "POST",
      success: function (response) { //Set the corresponding age group as active
        resultDiv.classList.remove("d-none");
        resultDiv.childNodes.forEach((child) => {
          if (child.nodeName == "UL") {
            child.childNodes.forEach((li) => {
              if (li.textContent.includes(response)) {
                li.classList.add("active");
              } else {
                if (li.classList) {
                  li.classList.remove("active");
                }
              }
            });
          }
        });
      },
      error: function (response) {
        alert(response, "danger");
      },
      complete: function (response) {
        // Show button text and hide spinner
        btn.innerHTML = "Predict Age";
      }
    });
  } else {
    alert("Please select an image to upload!", "danger");
  }
}

// Preview image before upload
function previewImage() {
  const preview = document.querySelector("#imagePreview");
  const file = document.querySelector("input[type=file]").files[0];
  const reader = new FileReader();

  if (file) {
    reader.readAsDataURL(file);
  }

  reader.addEventListener(
    "load",
    function () {
      preview.innerHTML = '<img src="' + reader.result + '" class="img-thumbnail">';
    },
    false
  );

  document.getElementById("result").classList.add("d-none"); // Hide result
}

// Bootstrap alert
const alert = (message, type) => {
  const alertPlaceholder = document.getElementById('alerts')
  const wrapper = document.createElement('div')
  wrapper.innerHTML = [
    `<div class="alert alert-${type} alert-dismissible fade show" role="alert">`,
    `   <div>${message}</div>`,
    '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
    '</div>'
  ].join('')

  alertPlaceholder.append(wrapper)
}