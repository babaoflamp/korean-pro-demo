/**
 * file util
 * file 기본 유틸
 *
 * 최초 작성자 - eun.jo
 *
 * 모듈 목록
 * fn_fileInit - 파일 init
 * fn_fileList(fileSeq) - 파일 리스트 조회
 * fn_fileDown(fileDtlSeq) - 파일 다운로드
 * fn_fileAdd() - 파일 input 추가 (최대 5개까지)
 * fn_fileDel(el) - 파일 input 삭제
 * fn_fileSave() - 파일 저장(등록/수정/삭제)
 * fn_fileValidation(el, files) - 파일 유효성 검사
 *
 * file util 사용시 jsp 설정
 * - 상세 조회시
 *  <c:set var="itemFile" value="info" scope="request"/>
 *	<jsp:include page="/WEB-INF/views/file/file.jsp" />
 *
 * - 등록시
 *  <c:set var="itemFile" value="regist" scope="request"/>
 *  <jsp:include page="/WEB-INF/views/file/file.jsp" />

 * - 수정시
 *  <c:set var="itemFile" value="edit" scope="request"/>
 *  <jsp:include page="/WEB-INF/views/file/file.jsp" />
 *
 * - 등록/수정시
 *   파일 시퀀스 input 태그 필요 <input type="hidden" name="fileSeq" id="fileSeq" value="">
 *   저장 폴더명 input 태그 필요 <input type="hidden" name="dirNm" id="dirNm" value="폴더명">
 *    - 해당 input 없을 경우 fileUtil.java측에 설정된 ETC 폴더로 파일 저장됨
 *   파일 등록/수정시 fn_fileSave() 호출하여 사용
 *    - 성공시 true, 실패시 실패 alert, false 반환
 */

let maxRequestSize; // 서버로 보낼수있는 파일 최대 크기
let maxFileSize;	// 단일 파일 최대 크기
// detach
let fileAddDetach;
let fileExistDetach;
let fileInfoDetach;

$(document).ready(function() {
	// 파일 init
	fn_fileInit();

	// 첨부파일 변경시
	$(document).on('change', '#fileGroup input[type=file]', function(e) {

		let files = e.target.files[0];

		// 파일 유효성 검사
		fn_fileValidation(this, files);
	});
});

/**
 * 파일 init
 * application.yml 설정된 maxRequestSize, maxFileSize 취득
 */
function fn_fileInit() {
	// 등록/수정 페이지일 경우
	if ( itemFile == 'regist' || itemFile == 'edit' ) {
		fileAddDetach = $('.fileAdd').detach();

		// 수정 페이지일 경우
		if ( itemFile == 'edit' )
			fileExistDetach = $('.fileExist').detach();

		// appliction.yml 값 취득 - byte로 변환해서 가져옴
		mz.util.ajaxObj('/api/multipart', '', 'GET', 'json', function fn_fileListCallback(data) {
			if ( data.status == 200 ) {
				maxRequestSize = data.data.maxRequestSize;
				maxFileSize = data.data.maxFileSize;
			}
	    }, false);
	}

	// 상세 페이지일 경우
	if ( itemFile == 'info' )
		fileInfoDetach = $('.fileLi').detach();
}

/**
 * 파일 리스트 조회
 * @param fileSeq - 파일 시퀀스
 */
function fn_fileList(fileSeq) {
	if ( fileSeq == null || fileSeq == '' )
		return;

	let params = {
		'fileSeq' : fileSeq
	};

	// 초기화
	if ( itemFile == 'info' )
		$('#fileList').empty();
	if ( itemFile == 'edit' )
		$('#fileGroup').empty();

	// core-util.js ajax
	mz.util.ajaxObj('/api/files', params, 'GET', 'json', function fn_fileListCallback(data) {
		let status = data.status;

        if ( status == 200 ) {

	        let list = data.data;

	        // 파일 상세
	        if ( itemFile == 'info' ) {
		        for (let i = 0; i < list.length; i++) {
		        	let fileInfo = fileInfoDetach.clone();

		        	fileInfo.find('a').attr('href', 'javascript:fn_fileDown('+list[i].fileDtlSeq+')');
		        	fileInfo.find('.fileNm').text(list[i].orignlFileNm);

		        	$('#fileList').append(fileInfo);
		        }
	        }

	        // 파일 수정
	        if ( itemFile == 'edit' ) {
	        	for (let i = 0; i < list.length; i++) {
		    		let fileExist = fileExistDetach.clone();

		    		fileExist.find('input[name=fileDtlSeq]').val(list[i].fileDtlSeq);
		    		fileExist.find('.fileNm').text(list[i].orignlFileNm);
		    		fileExist.find('.fileNm').data('fileSize', list[i].fileSize);

		    		$('#fileGroup').append(fileExist);
	        	}
	        }
        }
	}, false);
}

/**
 * 파일 다운로드
 * @param fileDtlSeq - 파일 상세 시퀀스
 */
function fn_fileDown(fileDtlSeq) {
	if ( fileDtlSeq == null && fileDtlSeq == '' )
		return;

	$.ajax({
        url: '/api/file/' + fileDtlSeq,
        type: 'GET',
        xhrFields: {
        	responseType: 'blob',
    	},
        success: function (data, status, xhr) {

			if ( status == 'success' ) {
				// 파일을 Blob 객체로 생성
		        let blob = new Blob([data]);

		     	// Blob으로부터 URL 생성
		        let url = window.URL.createObjectURL(blob);

		        // 다운로드 링크 생성
		        let a = document.createElement('a');
		        a.style.display = 'none';
		        a.href = url;

		        // 서버에서 Content-Disposition 헤더에 설정된 파일명 가져오기
		        let filename = '';
		        let disposition = xhr.getResponseHeader('Content-Disposition');
		        if (disposition && disposition.indexOf('attachment') !== -1) {
		            let filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
		            let matches = filenameRegex.exec(disposition);
		            if (matches != null && matches[1]) {
		                filename = decodeURIComponent(matches[1].replace(/['"]/g, ''));
		            }
		        }

				// 서버에서 설정된 파일명 사용
		        a.download = filename;
		        document.body.appendChild(a);

		        // 클릭 이벤트 트리거
		        a.click();

		        // 다운로드 후 링크 제거
		        window.URL.revokeObjectURL(url);
		        document.body.removeChild(a);
			} else {
				console.log('status: ' + status);
				alert('파일을 가져오는 데 실패했습니다.');
			}

        },
        error: function (xhr, textStatus, errorThrown) {
            alert('파일을 가져오는 데 실패했습니다.');
        }
    });
}

/**
 * 파일 input 추가(+버튼 클릭시) - (최대 5개까지)
 * maxFile - 업로드 가능한 file 수량
 */
function fn_fileAdd() {
	let fileCnt = $('.fileDiv').length;

	const maxFile = 5; // 최대 수량 변경시 해당 값 변경하면 됨
	if ( fileCnt == maxFile ) {
		alert('최대 ' + maxFile + '개의 파일만 등록할 수 있습니다.');
		return;
	}

	let fileAdd = fileAddDetach.clone();
	fileAdd.find('.fileLabel').attr('for', 'file'+fileCnt);
	fileAdd.find('.file').attr('id', 'file'+fileCnt);

	$('#fileGroup').append(fileAdd);
}

/**
 * 파일 input 삭제(-버튼 클릭시)
 */
function fn_fileDel(el) {
	$(el).closest('.fileDiv').remove();

	if ( $(el).closest('.fileDiv').find('.file').length > 0 ) {
		$('#fileGroup .fileDiv').each(function(index) {
			if ( $(this).find('.file').length > 0 ) {
				$(this).find('.file').attr('id', 'file'+index);
				$(this).find('.fileLabel').attr('for', 'file'+index);
			}
		});
	}
}

/**
 * 파일 저장(등록/수정/삭제)
 */
function fn_fileSave() {
	let flag = true;
	let formData = new FormData();

	let index = 0;
	$('#fileGroup input[type=file]').each(function () {
		if ( $(this)[0].files.length > 0 ) {
			formData.append('files['+index+']', $(this)[0].files[0]);
			index++;
		}
	});

	if ( $('#fileSeq').val() != '' ) {
		formData.append('fileSeq', $('#fileSeq').val());

		let fileDtlSeqArr = [];
		$('#fileGroup input[name=fileDtlSeq]').each(function () {
			fileDtlSeqArr.push($(this).val());
		});

		if ( fileDtlSeqArr.length > 0 )
			formData.append('fileDtlSeqArr', fileDtlSeqArr);
	}

	if ( $('#dirNm').length > 0 && $('#dirNm').val() != '')
		formData.append('dirNm', $('#dirNm').val());

	$.ajax({
	    type: ($('#fileSeq').val() != '') ? 'PATCH' : 'POST',
	    url: '/api/file',
	    processData: false,
	    contentType: false,
	    dataType : 'json' ,
		enctype: 'multipart/form-data',
	    data: formData,
	    async: false,
	    success: function(data) {
	        let status = data.status;

	        if ( status == 201 ) { // 정상 등록
	        	$('#fileSeq').val(data.data);
	        } else if ( status == 200 ) { // 정상 수정
	        } else if ( status == 204 ) { // 정상 삭제
	        	$('#fileSeq').val('');
	        } else {
	        	alert('처리 중 오류가 발생하였습니다.');
	        	flag = false;
	        }
	    },
	    error: function(xhr, status, error) {
	    	console.log(xhr);
			alert('처리 중 오류가 발생하였습니다.');
			flag = false;
	    }
	});

	return flag;
}

/**
 * 파일 유효성 검사
 * @param el - 파일 input (element)
 * @param files - 파일 객체
 */
function fn_fileValidation(el, files) {
	if ( files.size > maxFileSize ) {
		alert('업로드 가능한 파일의 최대 크기는 ' + maxFileSize + 'Byte 입니다.');
		return;
	}

	let totalFileSize = 0;
	let flag = true;
	$('.fileDiv').each(function() {
		if ( $(this).find('.fileNm').text() == files.name &&
				$(this).find('.fileNm').data('fileSize') == files.size ) {
			alert('중복 파일은 업로드할 수 없습니다.');
			flag = false;
			return false;
		}

		if (typeof $(this).find('.fileNm').data('fileSize') != 'undefined')
			totalFileSize += $(this).find('.fileNm').data('fileSize');
	});

	if ( !flag ) {
		$(el).val('');
		return;
	} else {
		if ( totalFileSize + files.size > maxRequestSize ) {
			alert('업로드 가능한 총 파일의 최대 크기는 ' + maxRequestSize +'Byte 입니다.');
			return;
		}
		$(el).closest('.fileDiv').find('.fileNm').text(files.name);
		$(el).closest('.fileDiv').find('.fileNm').data('fileSize', files.size);
	}
}