/**
 *  AI 한국어학습 (speechpro-ko)
 */

const audio = new Audio();

let questionBox = $('.question_detach').detach();
let resultBox = $('.result_detach').detach();

let isAllComplete = true;	// 모두 제출 여부
let loadQuestion;			// 진입 시 세팅할 문제
let isPlaying = false;		// 현재 오디오 재생중 여부

$(document).ready(function() {
	// 문제 목록
	fn_getQuestionList();

	// 문제 목록 - 문제 클릭 시
	$('.question_detach').on('click', function() {
		let koId = $(this).data('ko-id');
		recorder = null;
		evaluateData = null;

		fn_getQuestion(koId);
	})

    // 이전 버튼 클릭 시
    $('#prev').on('click', function() {
        moveToPrevious();
    });

    // 다음 버튼 클릭 시
    $('#next').on('click', function() {
        moveToNext();
    });

	// 제출하기
	$('#evaluate').on('click', function() {
		if ( evaluateData === undefined || evaluateData == null ) {
			$('#alertText').text('녹음을 완료해주세요.');
			openModal('alert-popup');

		} else {
			fn_evaluate();
		}
	})

	// 지난 결과 확인
	$('#previous').on('click', function() {
		fn_getPreviousResult();
	})

	// 다시 듣기 클릭 시
	$('#replay').on('click', function() {
		fn_getFile();
	})

	// 다시풀기 클릭 시 녹음 삭제
	$('#retry').on('click', function() {
		recorder = null;
		evaluateData = null;
	})

	// 결과창 다시풀기, x 버튼 클릭 시 오디오 정지
	$('.learning-popup .close-btn').on('click', function() {
		audio.pause();
		isPlaying = false;
	})

	// modal - 결과창 다음 문제 바로가기
	$('#popNext').on('click', function() {
		$('.learning-popup').hide();

		console.log($('.question_detach').last().hasClass('active'))

		moveToNext();
	})

});

// 문제 목록
function fn_getQuestionList() {

	mz.util.ajaxObj('/api/sp/ko/question', '', 'GET', 'json', fn_getQuestionList_callback, false);
}

// 문제 목록 callback
function fn_getQuestionList_callback(result) {
	$('#questionTarget').empty();

	if ( result.status == 200 ) {
		let data = result.data;

		data.forEach(function(item) {
			let box = questionBox.clone();
			let hasAnswered = item.hasAnswered;

			// 문제 세팅
			if ( hasAnswered !== true ) {
				isAllComplete = false;
				if ( loadQuestion === undefined ) {
					loadQuestion = item.koId;
				}
			}

			$(box).attr('data-ko-id', item.koId);
			$(box).find('.badges').addClass(hasAnswered === true ? 'st01' : 'st02');
			$(box).find('.badges').text(hasAnswered === true ? '제출완료' : '미제출');
			$(box).find('.order').text(item.order);
			$(box).find('.sentence').text(replaceSpace(item.sentence));
			$('#questionTarget').append(box);
		});

		if ( loadQuestion === undefined )
			loadQuestion = data[0].koId

		fn_getQuestion(loadQuestion);
		fn_paging();

	} else {
		$('#alertText').text('오류가 발생하였습니다.');
		openModal('alert-popup');
	}
}

// 문제 정보 1개
function fn_getQuestion(koId) {

	mz.util.ajaxObj('/api/sp/ko/question/' + koId, '', 'GET', 'json', fn_getQuestion_callback, false);
}

// 문제 정보 1개 callback
function fn_getQuestion_callback(result) {
	if ( result.status == 200 ) {
		let data = result.data;

		$('.question_detach').siblings().removeClass('active');
		$('.question_detach').each(function(idx, item) {
			if ( $(this).data('ko-id') == data.koId )
				$(this).addClass('active');
		})

		let hasAnswered = data.hasAnswered;

		$('#detail_order').text(data.order);
		$('#detail_sentence').text(replaceSpace(data.sentence));
		$('#detail_sentence').attr('data-detail-ko-id', data.koId);

		// 결과 확인창 다음 문제 버튼
		$('#popNext').show();
		if ( $('.question_detach:last-child').attr('data-ko-id') == data.koId ) {
			$('#popNext').hide();
		}

		fn_resetBadges(hasAnswered);
		fn_resetMicBtns(hasAnswered);
		fn_paging();

	} else {
		$('#alertText').text('오류가 발생하였습니다.');
		openModal('alert-popup');
	}
}

// 마이크 관련 버튼 초기화
function fn_resetMicBtns(hasAnswered) {
	// 제출 - 재녹음
	if ( hasAnswered ) {
		fn_setRerecordingBtn();

	// 미제출 - 초기 마이크
	} else {
		$('#arrorTxt').html(preTxt);
		$('#mic').prop('hidden', false);
		$('#recording').prop('hidden', true);

		$('#stopRecord').removeAttr('style');
		$('#stopRecord').addClass('disabled');
		$('#reRecord').attr('style', 'display:none;');

		$('#tooltip').prop('hidden', true);
	}
}

// 목록, 본문 뱃지 초기화
function fn_resetBadges(hasAnswered){
	// st~ class 삭제
	$('#detail_badge').removeClass(function(_, c) { return c.split(' ').find(cls => cls.startsWith('st')); });
	$('#detail_badge').addClass(hasAnswered === true ? 'st01' : 'st02');
	$('#detail_badge').text(hasAnswered === true ? '제출완료' : '미제출');

	let listBadge = $('.question_detach.active').find('.badges');
	listBadge.removeClass(function(_, c) { return c.split(' ').find(cls => cls.startsWith('st')); });

	// 목록 뱃지 update
	listBadge.addClass(hasAnswered === true ? 'st01' : 'st02');
	listBadge.text(hasAnswered === true ? '제출완료' : '미제출');

}

// 지난 결과
function fn_getPreviousResult() {
	let koId = $('#detail_sentence').attr('data-detail-ko-id');

	// 지난 결과
	mz.util.ajaxObj('/api/sp/ko/answer/' + koId + '/pre', '', 'GET', 'json', fn_getPreviousResult_callback, false);

}

// 지난 결과 callback
function fn_getPreviousResult_callback(result) {

	if ( result.status === 200 ) {
		fn_setResult( result.data );
		openModal('learning-popup');

	} else if ( result.status === 404 ) { // 지난결과 없음
		$('#alertText').text('지난 결과가 없습니다.');
		openModal('alert-popup');

	} else {
		$('#alertText').text('오류가 발생했습니다.');
		openModal('alert-popup');
	}
}

// 발음평가
function fn_evaluate() {
	openModal('loading-popup');

	let data = {
		'id' : $('#detail_sentence').attr('data-detail-ko-id'),
		'wav usr' :  evaluateData
	};
	setTimeout(function() {
		mz.util.ajaxObj('/api/sp/ko/evaluate', JSON.stringify(data), 'POST', 'json', fn_evaluate_callback, false);
	}, 500);
}

// 발음 평가 callback
function fn_evaluate_callback(result) {
	$('.loading-popup').hide();

	if ( result.status == 200 ) {
		openModal('checkResults-popup');
		$('#evaluateConfirm').click(function() {
			$('.checkResults-popup').css('display', 'none');

			fn_setResult( result.data );
			fn_getQuestion( $('#detail_sentence').attr('data-detail-ko-id') );

			openModal('learning-popup');
		})

	// 녹음 재진행
	} else {
		openModal('recording-popup');

		if ( $('#detail_badge').hasClass('st01') ) {
			fn_resetMicBtns(true);
		} else {
			fn_resetMicBtns(false);
		}
	}
}

// 학습 결과 세팅
function fn_setResult(data) {
	$('#resultTarget').empty();
	$('#resultTarget').attr('data-file-seq', data.fileSeq);

	// 파랑 step03: 70~100점 / 녹색 step02 : 50~69점 / 노랑 step01 : 0~49점
	let totalScore = Math.round(data.result.result.quality.sentences[1].score);
	$('#totalScore').html(totalScore);

	let submitDate = data.submittedAt;
	let formatDate = fn_getDate(submitDate);

	$('#date').html(formatDate);

	let words = data.result.result.quality.sentences[1].words;

	words.forEach(function(item) {
		if ( item.text !== '!SIL' ) { // 휴지구간 아닌것만
			let box = resultBox.clone();
			let score = Math.round(item.score);
			let level;
			if ( score >= 70 && score <= 100 ) {
				level = 'step03';
			} else if ( score >= 50 && score <= 69 ) {
				level = 'step02';
			} else {
				level = 'step01';
			}

			$(box).addClass(level);
			$(box).find('.learning_text').text(item.text);
			$(box).find('.learning_score').text(score);
			$('#resultTarget').append(box);
		}
	});

}

// 다시 듣기 음성 파일 요청
function fn_getFile() {

	if ( !isPlaying ) {
		let seq = $('#resultTarget').attr('data-file-seq');
		let data = {
			'fileSeq' : seq
		}
		mz.util.ajaxObj('/api/files', data, 'GET', 'json', fn_getFile_callback,  false);
	}
}

function fn_getFile_callback(result) {
	let dtlSeq = result.data[0].fileDtlSeq;

	$.ajax({
        url: '/api/file/' + dtlSeq,
        type: 'GET',
        xhrFields: {
        	responseType: 'blob',
    	},
        success: function (data, status, xhr) {
			if ( status == 'success' ) {
				// 이미 재생 중이면 새로 시작하지 않음
		        if ( isPlaying ) {
		            return;
		        }

	    		let audioURL = URL.createObjectURL(data);
				audio.src = audioURL;
	            audio.play();
	            isPlaying = true;

			} else {
				$('#alertText').text('오류가 발생하였습니다.');
				openModal('alert-popup');
			}
		}
	})
}

audio.pause = function() {
	isPlaying = false;
	audio.currentTime = 0;
	audio.src = '';
}

audio.onended = function() {
	isPlaying = false;
	audio.src = '';
}


// 이전, 다음 단계 버튼 처리
function fn_paging() {
    let questions = $('#questionTarget .question_detach');
    let activeQuestion = questions.filter('.active');

	$('#prev').removeClass('disabled');
	$('#next').removeClass('disabled');

    // 첫 번째 요소일 경우 이전 버튼 비활성화
    if ( questions.first().is(activeQuestion) ) {
		$('#prev').addClass('disabled');
		$('#next').removeClass('disabled');
    }
    // 마지막 요소일 경우 다음 버튼 비활성화
    if ( questions.last().is(activeQuestion) ) {
		$('#prev').removeClass('disabled');
		$('#next').addClass('disabled');
	}


}

// 버튼 클릭 시 이전/다음 요소로 이동
function moveToPrevious() {
    if ( $('#prev').hasClass('disabled') ) {
		return;
	}
    let activeQuestion = $('#questionTarget .question_detach').filter('.active');
    let prevQuestion = activeQuestion.prev('.question_detach');

    if ( prevQuestion.length ) {
        fn_getQuestion(prevQuestion.data('ko-id'))
	}
}

function moveToNext() {
	if ( $('#next').hasClass('disabled') ) {
		return;
	}

    let activeQuestion = $('#questionTarget .question_detach').filter('.active');
    let nextQuestion = activeQuestion.next('.question_detach');

    if ( nextQuestion.length ) {
		fn_getQuestion(nextQuestion.data('ko-id'))
    }
}

// 날짜 세팅
function fn_getDate(dateStr) {
    const date = new Date(dateStr);
	const formattedDate = date.toLocaleDateString('ko-KR', { year: 'numeric', month: 'long', day: 'numeric' });
	return formattedDate;
}

// &nbsp 대체
function replaceSpace(sentence) {
	return sentence.replaceAll(/\u00A0/g, " ");
}
