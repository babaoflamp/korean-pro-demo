/**
 * login 페이지 스크립트
 */


/* 로그인 성공시 이동 url */
const successUrl = '/';

let obj = new Object();

document.addEventListener("DOMContentLoaded", function () {
    $('.number-only').on('input', function(e) {
        let replace_text = $(this).val().replace(/[^0-9]/g, '');
        $(this).val(replace_text);
    });
});

function checkLogin() {

    // 입력 유무 확인
    if ( !validationFrom('loginForm')) return;

   //이메일 유효성
   const email = $("#email").val();
   const regEmail = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i

   if(!regEmail.test(email)) {
       $('#alertText').text('이메일 형식에 따라 정확히 입력해주세요.');
        openModal('alert-popup');
       return;
   }

	var formData = new FormData($("#loginForm")[0]);

    $.ajax({
        type: 'POST',
        url: '/api/login',
        dataType: 'json',
        data: formData,
        processData: false,
        contentType: false,
        async: true,

        success: function(data) {
			window.localStorage.setItem("name", encodeURI( data.data.name ) );
			window.localStorage.setItem("sysId", encodeURI( data.data.sysId ) );
			location.href = successUrl;
        },
        error: function(jqXHR, textStatus, errorThrown) {

            var msg = '';

            if (jqXHR.responseJSON) {
				msg = jqXHR.responseJSON.message;
            } else if (jqXHR.responseText) {
                msg = jqXHR.responseText;
            } else {
                msg = 'error';
            }
             $('#alertText').text(msg);

            openModal('alert-popup');
        }
    });
}


// 인증번호 발송
function sendNum(){

	if ( !validationFrom('sendNumForm')) return;

	let params = mz.util.getFormData('sendNumForm');

	openModal('numberCheck-popup');

	mz.util.ajaxObj('/api/login/createMail', JSON.stringify(params) , 'POST', 'json', function fn_joinCallback(data) {

		if (data.status != 200) {
       	 	$('.numberCheck-popup').fadeOut(300);
			$('#alertText').text('성명과 이메일 주소를 다시 확인해주세요.');
       	 	openModal('alert-popup');
		}else{
       	 	obj.sysId =  data.data;
			$('.passwordReset-popup').fadeOut(300);
		}

		$('#sendNumForm input').each(function() {
	        $(this).val('');
	    });
	});
}


function changepassWord(){

	let code = $("#code").val();

	if(!code){
		$('#alertText').text('인증번호를 다시 확인해주세요.');
   	 	openModal('alert-popup');
	}else{

		mz.util.ajaxObj('/api/login/findCodeCheck', code , 'POST', 'json', function fn_joinCallback(data) {

			if (data.status == 200) {
				openModal('numberCheck-popup');

				openModal('passwordChange-popup');
			    $('.numberCheck-popup').fadeOut(300);
			}else if(data.status == 408) {
				$('#alertText').text('인증 시간이 초과되었습니다.');
	       	 	openModal('alert-popup');
			}else{
				$('#alertText').text('인증번호를 다시 확인해주세요.');
	       	 	openModal('alert-popup');

			}
		});
	}
	$("#code").val('');
}

//비밀번호 재설정
function resetPwdEnc(){

	if ( !validationFrom('pwdChangeForm')) return;

	obj.newPwd =  $('#newPwd').val().trim();
	obj.confirmPwd = $('#confirmPwd').val().trim();

	mz.util.ajaxObj('/api/login/updatePwd', JSON.stringify(obj), 'POST', 'json', function fn_mypagePswdCallback(data) {
    	let status = data.status;

     	if ( status == 200 ) {
			$('#alertText').text('정상적으로 변경되었습니다.');
			location.reload();
        } else if ( status == 403 ) {
			$('#alertText').text('비밀번호가 일치하지 않습니다.');
		} else {
			$('#alertText').text('처리 중 오류가 발생하였습니다.');
        }
        openModal('alert-popup');
	}, false);
}

// 입력 유무 확인
function validationFrom(formId) {
    let flag = true;

    $('#' + formId + ' input').each(function() {
        if ( $(this).val().trim() === '' ) {
            let title = $(this).data('title');
            $('#alertText').text(title + '을(를) 입력해주세요.');
            openModal('alert-popup');
            flag = false;
            return false;
        }
    });
    return flag;
}

$(document).on("keyup", "#password, #resetEmail, #confirmPwd, #code", function (e) {
    if (e.keyCode == 13) {
        var actions = {
            "password": checkLogin,
            "resetEmail": sendNum,
            "confirmPwd": resetPwdEnc,
            "code": changepassWord
        };

        var action = actions[$(this).attr("id")];  // 현재 요소의 id 가져오기
        if (action) action();  // 함수 실행
    }
});