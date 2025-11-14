/**
 *
 */

let recorder;		// mediaRecorder
let evaluateData;	// 평가 데이터
let isComplete;		// 녹음완료 여부

const preTxt = "마이크를 클릭하면 <br class=\"pc-br\"> 녹음이 시작됩니다";
const recordingTxt = "녹음중";
const stopTxt = "정확도, 리듬에 유의하여 읽어보세요.";

/* device 활성  */
navigator.mediaDevices
	.getUserMedia( { audio : true })
	.then((stream) => {

		// 마이크 버튼
		$('#mic').on('click', function() {
			startRecording();
			isComplete = false;

			$('#arrorTxt').html(recordingTxt);

			$(this).prop('hidden', true);
			$('#recording').prop('hidden', false);

			// 녹음 완료 버튼 비활성 해제
			$('#stopRecord').removeClass('disabled');

			// arrow text 활성
			$('#preRecordingTxt').prop('hidden', true);
			$('#recordingTxt').prop('hidden', false);
		})

		// 녹음 완료 버튼
		$('#stopRecord').on('click', function() {
		    stopRecording();
		    isComplete = true;

			fn_setRerecordingBtn();
		})

		// 재녹음 버튼
		$('#reRecord').on('click', function() {
			recorder = null;
			evaluateData = null;

			startRecording();
			isComplete = false;

			$(this).prop('style', 'display:none;');
			// 녹음완료 버튼 보이기
			$('#stopRecord').removeAttr('style');
			$('#stopRecord').removeClass('disabled');
			// 녹음중 아이콘 보이기
			$('#recording').prop('hidden', false);

			// arrow text 활성
			$('#preRecordingTxt').prop('hidden', true);
			$('#recordingTxt').prop('hidden', false);
		})

		// 녹음 시작
		function startRecording() {

        	const mediaRecorder = new RecordRTC(stream, {
	    		type: 'audio',
	    		mimeType: "audio/wav",
	    		recorderType: StereoAudioRecorder,
	    		checkForInactiveTracks: true,
	    		numberOfAudioChannels: 1, // or leftChannel:true
	    		desiredSampRate: 16000,
	    		bufferSize: 16384,
	    		numberOfAudioChannels: 1
    		});

	        mediaRecorder.startRecording();
	        recorder = mediaRecorder;
	    }

	    // 녹음 중지
	    function stopRecording() {

	        recorder.stopRecording(function() {

	            const blob = recorder.getBlob();
				let audioUrl = URL.createObjectURL(blob);

	            if (!(blob instanceof Blob)) {
	                alert("녹음 데이터가 유효하지 않습니다.");
	                return;
	            }
	            // 툴팁 활성
				$('#tooltip').removeAttr('hidden');

				fn_processRecording(blob);

	        });
	    };

	})
	.catch((err) => {

		if ( err.name == "NotAllowedError" ) {
			console.error("Error: Permission denied by the user.");
			alert('시스템 마이크 접근 권한을 확인해주세요.');

		} else if ( err.name == "NotFoundError" || err.name == "DevicesNotFoundError" ) {
		    console.error("Error: No matching devices found.");
			alert('마이크를 사용할 수 없는 장치입니다.');

		} else {
			console.error("An unexpected error occurred: ", error);
			alert('마이크를 사용할 수 없습니다.');
		}

});

// 재녹음 버튼
function fn_setRerecordingBtn() {
	$('#arrorTxt').html(stopTxt);

	// 마이크 비활, 녹음중 활성
	$('#mic').prop('hidden', true);
	$('#recording').prop('hidden', false);

	// 녹음완료 비활, 재녹음 버튼 활성
	$('#reRecord').removeAttr('style');
	$('#stopRecord').prop('style', 'display:none;');

	// arrow text 활성
	$('#preRecordingTxt').prop('hidden', true);
	$('#recordingTxt').prop('hidden', false);
}

// 녹음 blob 가공
function fn_processRecording(blob) {
    const base64Audio = blobToBase64(blob);

    base64Audio.then((data) => {
		evaluateData = data;
	}).catch((error) => {
	    console.error("Base64 변환 중 오류 발생:", error);
	});
}

// blob -> base64
function blobToBase64(blob) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(blob);
        reader.onloadend = () => {
            const base64String = reader.result.split(',')[1]; // MIME 타입 제거
            resolve(base64String);
        };
        reader.onerror = (error) => reject(error);
    });
}
