/**
 *  chatbot module
 */
let userBox = $('.user-message').detach();
let chatBox = $('.chat-message').detach();

$(document).ready(function() {

	fn_setMsg('chat', '무엇이든 물어보세요.');

	// 입력창 keypress event
	$('#chatInput').on('keypress', function(e) {
		if( e.which === 13 ) {
			e.preventDefault();
			fn_chatRequest($(this).val());
		}
	})

	// 전송 버튼
	$('#submitBtn').on('click', function() {
		fn_chatRequest();
	})

	// stt 녹음시작
	$('#startRecord').on('click', function() {
		$('#stopRecord').removeAttr('hidden');
		$(this).attr('hidden', true);
	})

	// stt 녹음중지
	$('#stopRecord').on('click', function() {
		$('#startRecord').removeAttr('hidden');
		$(this).attr('hidden', true);
	})

})

function fn_setMsg(type, msg) {

	if ( type === 'user' ) {
		box = userBox.clone()
		$('#chatInput').empty();
	} else {
		box = chatBox.clone();
	}

	box.find('p').text(msg);
	$('.chat-content').append(box);

	let container = $('.chat-content');
	container.scrollTop(container[0].scrollHeight)
}

function fn_chatRequest() {

	if ( !fn_validation() ) {
		return false;
	}
	$('.loading-popup').show();

	let input = $('#chatInput').val();
	fn_setMsg('user', input);

	let data = new Object();
	data.query = input;

	setTimeout(function() {
		mz.util.ajaxObj('/api/mirage/chat', JSON.stringify(data), 'POST', 'json', fn_chatResponse, false);
	}, 700);
}

function fn_chatResponse(result) {
	$('.loading-popup').hide();

	console.log(result);

	if ( result.status === 200 ) {
		let response = result.data.Response;

		$('#chatInput').empty();
		fn_setMsg('chat', response);

	} else {
		fn_setMsg('chat', '응답 생성에 실패하였습니다.');
	}
}

function fn_validation() {
	if ( mz.util.isEmpty($('#chatInput').val()) ) {
		$('#chatInput').focus();
		alert('대화를 입력해주세요.');
		return false;
	}

	return true;
}

// stt response
function fn_setResult(result) {
	result.forEach(function(item) {
		let text = item.stt[0].text;
		let textResult = $('#chatInput').text();
		$('#chatInput').val(textResult + " " + text);
	});
}