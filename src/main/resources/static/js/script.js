function predict_age() {
  var img = document.getElementById("faceImage").files[0];
  var btn = document.getElementById("predictBtn");
  
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
      success: function (response) {
        document.getElementById("result").innerHTML = response;
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