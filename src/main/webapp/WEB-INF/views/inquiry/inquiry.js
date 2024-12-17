const maxLength = 300;
let selectedFiles = []; // 선택된 파일을 저장할 배열
let isDropDown = true;

function inquiryContentHandler() {
  let inquiryContent = document.querySelector('#inquiry-content');
  let errorMessageElement = document.querySelector('.error-message');
  let charCountElement = document.querySelector('.char-count');
  errorMessageElement.style.color = 'red';
  charCountElement.style.color = 'black';
  let contentLength = inquiryContent.value.length;

  // 글자수 업데이트
  charCountElement.textContent = contentLength;

  if (contentLength < 10) {
      errorMessageElement.innerHTML = '<div class="error-icon"></div>' + '<div style="display:inline-block;">10자 이상의 문의 내용을 입력해 주세요.</div>';
      errorMessageElement.style.visibility = 'visible'; // Show message
  } else {
      errorMessageElement.style.visibility = 'hidden'; // Hide message
  }

  if (contentLength >= 10 && contentLength < maxLength) {
      errorMessageElement.style.visibility = 'hidden'; // Hide message
  } else if (contentLength == maxLength) {
      charCountElement.style.color = 'red';
  } else if (contentLength > maxLength) {
      charCountElement.style.color = 'red';
      setupTextAreaLimitWithModal();
  }
}


function setupTextAreaLimitWithModal() {
const textarea = document.getElementById('inquiry-content');
const charCount = document.querySelector('.char-count');
const modal = document.getElementById('maxLengthModal');
const closeModal = document.getElementById('closeModal');


function updateCharCount() {
    const currentLength = textarea.value.length;
    charCount.textContent = currentLength;

    if (currentLength > maxLength) {
        modal.style.display = 'block';
        textarea.value = textarea.value.slice(0, maxLength); // 300자로 잘라냄
    }
}

textarea.addEventListener('input', updateCharCount);

closeModal.addEventListener('click', function() {
    modal.style.display = 'none';
});
}



function inquiryCategoryHandler() {
  let arrowDown = document.querySelector('.select-arrow');
  let arrowUp = document.querySelector('.select-arrow2');

  if(isDropDown) {
    arrowDown.style.visibility = 'hidden';
    arrowUp.style.visibility = 'visible';
    isDropDown = false;
  }
  else {
    arrowDown.style.visibility = 'visible';
    arrowUp.style.visibility = 'hidden';
    isDropDown = true;
  }
}





function init() {
  document.querySelector('#inquiry-content').addEventListener('input', inquiryContentHandler);

  document.querySelector('.form-select').addEventListener('click', inquiryCategoryHandler);

  document.getElementById('file-input').addEventListener('change', function(event) {
      var imagePreview = document.getElementById('image-preview');
      
      // 선택된 파일들을 배열로 변환
      var files = Array.from(event.target.files);

      // 현재 선택된 파일의 총 개수
      const totalFiles = selectedFiles.length + files.length;

      // 최대 3개까지만 유지
      if (totalFiles > 3) {
          alert("최대 3개까지만 첨부할 수 있습니다.");
          return; // 새로운 파일을 추가하지 않음
      }

      // 선택된 파일들을 기존 배열에 추가
      selectedFiles = selectedFiles.concat(files);

      // 미리보기 업데이트
      updateImagePreview(imagePreview);
      
      // 파일 개수 업데이트
      document.getElementById('file-count').textContent = selectedFiles.length;
  });

  // 미리보기 업데이트 함수
  function updateImagePreview(imagePreview) {
      imagePreview.innerHTML = ''; // 기존 미리보기 삭제

      selectedFiles.forEach(function(file, index) {
          var reader = new FileReader();

          reader.onload = function(e) {
              var imgContainer = document.createElement('div');
              imgContainer.style.position = 'relative';
              imgContainer.style.display = 'inline-block';
              imgContainer.style.margin = '5px';

              var img = document.createElement('img');
              img.src = e.target.result;
              img.style.width = '120px'; // 미리보기 크기 조정
              img.style.height = '120px';

              var deleteButton = document.createElement('button');
              deleteButton.textContent = 'X';
              deleteButton.style.position = 'absolute';
              deleteButton.style.top = '0';
              deleteButton.style.right = '0';
              deleteButton.style.backgroundColor = 'black';
              deleteButton.style.background = 'rgba(0, 0, 0, 0.4)';
              deleteButton.style.borderRadius = '6px';
              deleteButton.style.color = 'white';
              deleteButton.style.border = 'none';
              deleteButton.style.cursor = 'pointer';

              // 삭제 버튼 클릭 시 동작
              deleteButton.addEventListener('click', function() {
                  selectedFiles.splice(index, 1); // 선택된 파일에서 해당 파일 제거
                  updateImagePreview(imagePreview); // 미리보기 업데이트
                  document.getElementById('file-count').textContent = selectedFiles.length; // 파일 개수 업데이트
              });

              imgContainer.appendChild(img);
              imgContainer.appendChild(deleteButton);
              imagePreview.appendChild(imgContainer);
          };

          reader.readAsDataURL(file); // 파일을 읽어 Data URL로 변환
      });
  }
}

window.addEventListener('load', init);