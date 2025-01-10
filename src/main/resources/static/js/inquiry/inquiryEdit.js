const maxLength = 300;
let selectedFiles = []; // 선택된 파일을 저장할 배열
let deletedFiles = []; // 삭제된 파일 ID를 저장할 배열
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
    
    if (isDropDown) {
        arrowDown.style.visibility = 'hidden';
        arrowUp.style.visibility = 'visible';
        isDropDown = false;
    } else {
        arrowDown.style.visibility = 'visible';
        arrowUp.style.visibility = 'hidden';
        isDropDown = true;
    }
}

function validateForm(event) {
  event.preventDefault(); // 기본 제출 동작 방지

  //const inquiryTitle = document.getElementById('inquiry-title').value.trim();
  const inquiryContent = document.getElementById('inquiry-content').value.trim();
  let isValid = true;

  // 제목 유효성 검사
  //if (inquiryTitle.length < 5) {
//    alert('제목은 5자 이상 입력해주세요.');
//    isValid = false;
//  }

  // 내용 유효성 검사
  if (inquiryContent.length < 10) {
    alert('문의 내용은 10자 이상 입력해주세요.');
    isValid = false;
  }

  // 모든 유효성 검사를 통과하면 폼 제출
  if (isValid) {
    event.target.submit();
  }
}

function init() {
	
	// 폼 제출 이벤트 리스너 추가
	document.querySelector('form').addEventListener('submit', validateForm);
	
    document.querySelector('#inquiry-content').addEventListener('input', inquiryContentHandler);
    document.querySelector('.form-select').addEventListener('click', inquiryCategoryHandler);

    // 기존 이미지 로드 및 삭제 버튼 추가
    loadExistingImages();

    document.getElementById('file-input').addEventListener('change', handleFileSelect);
}

function loadExistingImages() {
    const existingImagesContainers = document.querySelectorAll('#image-preview img');

    existingImagesContainers.forEach((img, index) => {
        selectedFiles.push({
            name: img.getAttribute('data-filename'),
            src: img.src,
            id: img.getAttribute('data-id') // 기존 이미지 ID를 가져옴
        });

        const deleteButton = createDeleteButton(index, true); // 기존 이미지 삭제 버튼 생성

        const imgContainer = img.parentElement;
        imgContainer.appendChild(deleteButton);
        
        imgContainer.style.position = 'relative'; // Ensure the container has relative positioning for the button
    });

    updateFileCount();
}

function handleFileSelect(event) {
    const files = Array.from(event.target.files);
    
    const totalFiles = selectedFiles.length + files.length;

    if (totalFiles > 3) {
        alert("최대 3개까지만 첨부할 수 있습니다.");
        return; // 새로운 파일을 추가하지 않음
    }

    selectedFiles.push(...files);
    
    updateImagePreview();
}

function updateImagePreview() {
    const imagePreview = document.getElementById('image-preview');
    
    imagePreview.innerHTML = ''; // 기존 미리보기 삭제

    selectedFiles.forEach((file, index) => {
        const imgContainer = createImageContainer(file, index);
        imagePreview.appendChild(imgContainer);
    });

    updateFileCount();
}

function createImageContainer(file, index) {
   const imgContainer = document.createElement('div');
   imgContainer.style.position = 'relative';

   const img = document.createElement('img');
   img.src = file.src || URL.createObjectURL(file);
   img.style.width = '120px'; // 미리보기 크기 조정
   img.style.height = '120px';

   const deleteButton = createDeleteButton(index, false); // 새로 선택한 이미지 삭제 버튼 생성

   imgContainer.appendChild(img);
   imgContainer.appendChild(deleteButton);

   return imgContainer;
}

function createDeleteButton(index, isExisting) { // isExisting: 기존 이미지인지 여부
   const deleteButton = document.createElement('button');
   deleteButton.textContent = 'X';
   deleteButton.style.position = 'absolute';
   deleteButton.style.top = '0';
   deleteButton.style.right = '0';
   deleteButton.style.backgroundColor = 'rgba(0, 0, 0, 0.4)';
   deleteButton.style.borderRadius = '6px';
   deleteButton.style.color = 'white';
   deleteButton.style.border = 'none';
   deleteButton.style.cursor = 'pointer';

   deleteButton.addEventListener('click', () => {
       if (isExisting) { // 기존 이미지인 경우
           deletedFiles.push(selectedFiles[index].id); // 삭제된 파일 ID를 추가
       }
       selectedFiles.splice(index, 1); // 선택된 파일에서 해당 파일 제거
       updateImagePreview(); // 미리보기 업데이트
       updateFileCount(); // 파일 개수 업데이트
   });

   return deleteButton;
}

function updateFileCount() {
   document.getElementById('file-count').textContent = selectedFiles.length;
}

// 페이지 로드 시 초기화 함수 호출
window.addEventListener('load', init);
