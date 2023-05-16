// Send request to server to classify text
function classify_text() {
    var text = document.getElementById("text").value;
    var result_paragraph = document.getElementById("result");
  
    if (text != "") {
      var btn = document.getElementById("classify_btn");
      btn.classList.add("button--loading");
      btn.textContent = ''
      $.ajax({
        url: "sentiment", // point to server-side URL
        dataType: "text", // what to expect back from server
        cache: false,
        contentType: false,
        processData: false,
        data: "text="+text,
        type: "get",
        success: function (response) {
          responseObj = JSON.parse(response) // parse JSON response
          positiveResult = responseObj.find(item=>item.className=="Positive") // find positive result
          negativeResult = responseObj.find(item=>item.className=="Negative") // find negative result
          result_paragraph.innerHTML = `<span class="badge bg-success">${positiveResult.className}</span> <span class="badge bg-danger">${negativeResult.className}</span> <br><br>` +
            `<div class="progress">` +
            `<div class="progress-bar progress-bar-striped progress-bar-animated bg-success" role="progressbar" aria-label="Positive probability" style="width: ${positiveResult.probability * 100}%">${Math.round((positiveResult.probability * 100) * 100) / 100}%</div>` +
            `<div class="progress-bar progress-bar-striped progress-bar-animated bg-danger" role="progressbar" aria-label="Negative probability" style="width: ${negativeResult.probability * 100}%">${Math.round((negativeResult.probability * 100) * 100) / 100}%</div>` +
            `</div>` // create progress bars and spans for both classes
        },
        error: function (response) {
          alert(response, "danger");
        },
        complete: function (response) {
          btn.classList.remove("button--loading")
          btn.textContent = 'Classify text'
        }
      });
    } else {
      alert("Please enter some text!", "danger");
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