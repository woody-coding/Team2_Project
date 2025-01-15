/**
 * 
 */
 // DOM이 완전히 로드된 후 실행되도록 수정
document.addEventListener('DOMContentLoaded', function() {
    // span 요소들을 모두 가져옴
    const editableSpans = document.querySelectorAll('.editable');
    
    editableSpans.forEach(span => {
        span.onclick = function() {
            const textarea = document.createElement('textarea');
            textarea.className = 'auto-resize';
            textarea.value = this.textContent;
            
            textarea.addEventListener('blur', function() {
                span.textContent = this.value;
                span.style.display = 'inline';
                this.remove();
            });

            textarea.addEventListener('input', function() {
                this.style.height = 'auto';
                this.style.height = this.scrollHeight + 'px';
            });

            this.style.display = 'none';
            this.parentNode.insertBefore(textarea, this);
            textarea.focus();
        };
    });
});

document.querySelectorAll('.auto-resize').forEach(textarea => {
    textarea.addEventListener('input', function() {
        this.style.height = 'auto';
        this.style.height = this.scrollHeight + 'px';
    });
});


//파일 관련
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('actor_img').addEventListener('click', function() {
        document.getElementById('fileInput').click();
    });

    document.getElementById('fileInput').addEventListener('change', function(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                document.querySelector('.actor_img').style.backgroundImage = `url(${e.target.result})`;
            };
            reader.readAsDataURL(file);
        }
    });
 // 폼 제출 전에 파일이 선택되었는지 확인
    document.querySelector('form').addEventListener('submit', function(event) {
        const fileInput = document.getElementById('fileInput');
        if (fileInput.files.length === 0) {
            event.preventDefault(); // 제출 막기
            alert('파일을 선택해야 합니다!');
        }
    });
});
 