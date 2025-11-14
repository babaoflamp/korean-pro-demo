/* [문제점]
	1. 문장뒤에 온점없음(stt 데모 사이트에는 나옴)
	2. 정렬안돼서 cnt값으로 정렬 따로 처리함
*/

let ws = null; 			// 연결한 web socket
let recorder;			// recorder 객체
let items = [];			// stt 응답결과 담을 배열

const vthres = 0.65;
const vsvd = 150;
const vesd = 150;
const vcut = 150;
const threshold = -1;
const vms = 500;
const vme = 500;

navigator.mediaDevices.getUserMedia( { audio : true })
	.then((stream) => {

		// Start 버튼 클릭 시 녹음 시작
		document.getElementById('startRecord').addEventListener('click', function() {
		    startRecording();
		});

		// Stop 버튼 클릭 시 녹음 종료
		document.getElementById('stopRecord').addEventListener('click', function() {
		    stopRecording();
		});

		// 녹음 시작
		function startRecording() {
			connectSocket();

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
				// TEST
				/*fetch('/js/experience/ttsmaker.wav')  //
		           .then(response => response.blob())  //
		           .then(blob => {
		              // Blob을 Object URL로 변환
						socketSend(blob);
						document.getElementById('audio_file').src = URL.createObjectURL(blob);
					})
					.catch(error => {
		                console.error('Error fetching the audio file:', error);
					});*/

	            const blob = recorder.getBlob();
				let audioUrl = URL.createObjectURL(blob);

	            if (!(blob instanceof Blob)) {
	                alert("녹음 데이터가 유효하지 않습니다.");
	                return;
	            }
	            socketSend(blob);

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

/* websocket connect */
function connectSocket() {
	var wsUri = (location.protocol == "http:")
		? "ws://" + host + ":" + port + "/ws"
		: "wss://" + host + ":" + port + "/ws";

	ws = new WebSocket(wsUri);
	ws.binaryType = 'arraybuffer';
	ws.onopen = function(evt) { on_open(evt) };			// 연결
	ws.onclose = function(evt) { on_close(evt) };		// 연결 종료
	ws.onmessage = function(evt) { on_message(evt) };	// 메세지 수신
	ws.onerror = function(evt) { on_error(evt) };		// 오류
}

/* socket open event */
function on_open(evt) {
	console.log("[open] : "+JSON.stringify(evt));

	if (ws.readyState == 0) {
        console.log("[open] : websocket - connection has not been established");

    } else if (ws.readyState == 1) {
		console.log('[open] websocket - connection success')

		fname = document.getElementById('audio_file');

    	// 소켓이 열리면, file 임을 알림
        ws.send('{"language":"ko","intermediates":false,"cmd":"fjoin","vad_thres":'+ vthres +',"vad_svd":'+vsvd+',"vad_esd":'+vesd+',"vad_cut":'+vcut+',"threshold":'+threshold+',"vad_ms":'+vms+',"vad_me":'+vme+',"fname":"'+fname+'","view":true}');

    } else if (ws.readyState == 2) {
        console.log("[open] : websocket - connection is going through the closing handshake");

    } else if (ws.readyState == 3) {
        console.log("[open] : websocket - connection has been closed or could not be opend");
    } else {}
}

/* socket close event*/
function on_close(event){
	console.log("[close] "+ JSON.stringify(event));
	if(event.code == 1006){
		alert("연결이 비정상적으로 종료되었습니다. \n마이크가 연결되어있는지 확인하세요.");
	}
}

/* socket message 수신*/
function on_message(evt){
	let data = JSON.parse(evt.data);
	let payload = data.payload;

	console.log("[message] reciveMessage "+ evt.data);

	// 연결 성공
	if( data.event == 'reply') {
		console.log('CONNECTION SUCCESS')

	} else if (data.event == 'file') {
		id = '';
		this.postMessage({
			command : "file",
			idx : data.cnt,
			wav : data.fname
		});

	// 인식 결과
	} else if ( payload.stt ) {
		items.push(payload);

	// 연결 종료
	} else if ( data.event == "close" ) {
		// 완료 시 cnt로 정렬 후 결과값 세팅
		if ( items.length != 0 ) {
			items.sort((a, b) => a.cnt - b.cnt);
			fn_setResult(items);
			items = [];

		} else {
			alert("인식된 결과가 없습니다.");
		}

		if ( payload.status ) {
			api_status = payload.status;
			this.postMessage({
				command : "asr",
				idx : data.cnt,
				result : payload.status,
				epd : true
			});

		} else {
			// 마지막(발화점)일 경우 epd : true
			// epd 안 잡히고 종료 or 정상종료
			this.postMessage({
				command : "asr",
				idx : data.cnt,
				result : "",
				epd : true
			});
			ws.close();
		}
	}

}

/* socket error event */
function on_error(evt){
	console.log("[on_error] " + JSON.stringify(evt.data));
	ws.close();
	ws = null;
}

/* socket 메세지 전송 */
function socketSend(item) {
	if ( ws ) { // 소켓이 열려있으면
		let state = ws.readyState;

		if (state == 1) {
			// blob 전송
			if (item instanceof Blob) {
				if (item.size > 0) {
					ws.send(item);
				} else {
					console.error('item size 0')
				}
			} else {
				if (item.size > 0)
					ws.send(item);
			}
		} else {
			console.error("websocket is not ready to send")
		}
	}
}
