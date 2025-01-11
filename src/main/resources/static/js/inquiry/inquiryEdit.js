let selectedFiles = []; // 선택된 파일을 저장할 배열
let existingFileIds = []; // 기존 파일 IDs를 저장할 배열
let existingFilesData = []; // 기존 파일 데이터 저장
let newFiles = []; // 새로 추가된 파일을 저장할 배열

function init() {
    document.querySelector('form').addEventListener('submit', validateForm);
    document.querySelector('#inquiry-content').addEventListener('input', inquiryContentHandler);
    document.querySelector('.form-select').addEventListener('click', inquiryCategoryHandler);
    loadExistingImages();
    document.getElementById('file-input').addEventListener('change', handleFileSelect);
}

function loadExistingImages() {
    const existingImagesContainers = document.querySelectorAll('#image-preview img');
    existingImagesContainers.forEach((img) => {
        const fileId = img.getAttribute('data-id');
        const fileName = img.getAttribute('data-filename') || img.src.split('/').pop();
        
        existingFileIds.push(fileId);
        existingFilesData.push({ id: fileId, name: fileName });

        selectedFiles.push({
            id: fileId,
            name: fileName,
            src: img.src
        });

        const deleteButton = createDeleteButton(selectedFiles.length - 1, true);
        const imgContainer = img.closest('li') || img.parentElement;
        imgContainer.appendChild(deleteButton);
        imgContainer.style.position = 'relative';
    });
    updateFileCount();
}

function handleFileSelect(event) {
    const files = Array.from(event.target.files);
    
    if (selectedFiles.length + files.length > 3) {
        alert("최대 3개까지만 첨부할 수 있습니다.");
        return;
    }

    files.forEach(file => {
        const isDuplicate = selectedFiles.some(selectedFile => selectedFile.name === file.name);
        
        if (!isDuplicate) {
            const fileObj = {
                id: null,
                name: file.name,
                file: file
            };
            selectedFiles.push(fileObj);
            newFiles.push(file);
        } else {
            alert(`${file.name} 파일은 이미 추가되었습니다.`);
        }
    });
    
    updateImagePreview();
}

function validateForm(event) {
    event.preventDefault();

    const inquiryTitle = document.getElementById('inquiry-title').value.trim();
    const inquiryContent = document.getElementById('inquiry-content').value.trim();
    let isValid = true;

    if (inquiryTitle.length < 5) {
        alert('제목은 5자 이상 입력해주세요.');
        isValid = false;
    }

    if (inquiryContent.length < 10) {
        alert('문의 내용은 10자 이상 입력해주세요.');
        isValid = false;
    }

    if (isValid) {
        const formData = new FormData(event.target);
		
        existingFileIds.forEach(id => {
            formData.append('existingFileIds', id);
        });

        newFiles.forEach(file => {
            formData.append('newFiles', file);
        });

        fetch(event.target.action, {
            method: 'POST',
            body: formData
        }).then(response => {
            if (response.ok) {
                window.location.href = '/inquiry';
            } else {
                alert('문의 등록/수정에 실패했습니다.');
            }
        });
    }
}

function inquiryContentHandler() {
   let inquiryContent = document.querySelector('#inquiry-content');
   let errorMessageElement = document.querySelector('.error-message');
   let charCountElement = document.querySelector('.char-count');
   errorMessageElement.style.color = 'red';
   charCountElement.style.color = 'black';
   let contentLength = inquiryContent.value.length;

   charCountElement.textContent = contentLength;

   if (contentLength < 10) {
       errorMessageElement.innerHTML = '<div class="error-icon"></div>' + '<div style="display:inline-block;">10자 이상의 문의 내용을 입력해 주세요.</div>';
       errorMessageElement.style.visibility = 'visible';
   } else {
       errorMessageElement.style.visibility = 'hidden';
   }

   if (contentLength >= 10 && contentLength < maxLength) {
       errorMessageElement.style.visibility = 'hidden';
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
           textarea.value = textarea.value.slice(0, maxLength);
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

function updateImagePreview() {
   const imagePreview = document.getElementById('image-preview');
   imagePreview.innerHTML = '';

   selectedFiles.forEach((fileObj, index) => {
       const imgContainer = createImageContainer(fileObj, index);
       imagePreview.appendChild(imgContainer);
   });

   updateFileCount();
}

function createImageContainer(fileObj, index) {
   const imgContainer = document.createElement('div');
   imgContainer.style.position = 'relative';

   const img = document.createElement('img');
   img.src = fileObj.src || URL.createObjectURL(fileObj.file);
   img.style.width = '120px';
   img.style.height = '120px';

   const deleteButton = createDeleteButton(index, fileObj.id !== null);

   imgContainer.appendChild(img);
   imgContainer.appendChild(deleteButton);

   return imgContainer;
}

function createDeleteButton(index, isExisting) {
    const deleteButton = document.createElement('button');
    deleteButton.textContent = 'X';
    deleteButton.style.position = 'absolute';
    deleteButton.style.top = '0';
    deleteButton.style.right = '0';
    deleteButton.style.backgroundColor = 'rgba(0,0,0,0.4)';
    deleteButton.style.borderRadius = '6px';
    deleteButton.style.color = 'white';
    deleteButton.style.border = 'none';
    deleteButton.style.cursor = 'pointer';

    deleteButton.addEventListener('click', (e) => {
        e.preventDefault();
        
        const fileToDelete = selectedFiles[index];

        selectedFiles.splice(index, 1);

        if (isExisting) {
            existingFileIds = existingFileIds.filter(id => id !== fileToDelete.id);
        } else {
            newFiles = newFiles.filter(file => file.name !== fileToDelete.name);
        }

        updateImagePreview();
        updateFileCount();
    });

    return deleteButton;
}

function updateFileCount() {
   document.getElementById('file-count').textContent = selectedFiles.length;
}

window.addEventListener('load', init);
