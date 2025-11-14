/**
 * mz_web Framework js_util
 * js 기본 유틸
 *
 * 최초 작성자 - Boryeong
 *
 * 모듈 목록
 * ajaxObj  - AJAX(Object)
 * ajaxForm - AJAX(FormData)
 * isEmpty -  null, undefined 체크
 * sendToPost - form hidden 통신
 * prependZero - 공백 0치환
 * comma - 콤마생성
 * uncomma - 콤마제거
 * nullToBlank - null값 공백치환
 * camelCase - 카멜케이스 변환
 * replaceValue - 특정 문자열 replace
 * emailCheck - 이메일 체크
 * getFormData - form input 태그 Object로 변환
 * resetForm - form 초기화
 * jsonToSelectBox - selectBox 생성
 * codeToSelectBox - 코드 그룹으로 하위 코드 selectBox 생성
 * fetchObj - Fetch(Object)
 * addClass - class 추가
 * removeClass - class 제거
 * addCookie - 쿠키 추가
 * removeCookie - 쿠키 제거
 * getCookie - 쿠키 가져오기
 * isValidIdNumber - 주민등록번호 체크
 * hasKoreanCharacter - 한국어 포함 체크
 * isAlphanumeric - 영문 대소문자와 숫자로만 구성된 문자열 패턴 검사
 * isValidPhoneNumber - 번호 체크
 * isValidMobileNumber - 핸드폰 번호 체크
 * getResultList - 테이블 데이터 가져오기
 * drawResultList - 데이터를 페이징,출력할 목록 처리
 * calculatePagination - 페이징 계산
 * drawPaging - 페이징 그리기
 * getUserNm - 로그인 사용자 이름
 * inputKeyType - 입력키 제어
 * onEnterKey - 버튼 enter 적용 및 함수 실행
 * checkReqInput - required 클래스를 가진 input 빈값 체크
 * calculateDateFromToday - 오늘날짜로부터 일/주/월/연 단위 날짜 계산
 * downloadListToExcel - 목록 엑셀 다운로드
 * init - init 설정
 *
 */
$(document).ready(function() {
	mz.util.init();
});

if (typeof window.mz === 'undefined') {
	window.mz = new Object();
}

// 중앙 유틸 함수 모듈
window.mz.util = (function() {

	/**
	* (FormData)AJAX호출 (기본 비동기)
	* promise 사용
	* ex) mz.util.ajaxForm(url,formParams).done(function(data){});
	* 필수 인자 : ajaxUrl
	* @param {String} ajaxUrl - 통신할 URL
	* @param {Form} formParams - 전달할 파라미터 ex) var formTemp = $('#formTemp');
	* @param {String} httpMethod - 통신할 httpMethod
	* @param {Form} dataType - 통신할 dataType ex) 'json'
	* @param {String} callback - 콜백 함수 (선택)
	* @param {boolean} async - 동기(true), 비동기 (false)
	* @returns
	*/
	function ajaxForm(ajaxUrl, formParams, httpMethod, dataType, callback, async) {
		var dfd = $.Deferred();

		if (!isEmpty(async)) {
			async = true;
		}

		if (isEmpty(dataType)) {
			dataType = 'json';
		}

		if (isEmpty(httpMethod)) {
			httpMethod = 'POST';
		}

		if (!isEmpty(ajaxUrl)) {

			var formData = new FormData($(formParams)[0]); // 폼 FormData로 변환

			$.ajax({
				type: httpMethod,
				url: ajaxUrl,
				dataType: dataType,
				data: formData,
				processData: false,
				contentType: false,
				async: async,

				success: function(data) {
					if (typeof callback === 'function') {
						return callback(data);
					}

					dfd.resolve(data);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					console.log('error :', ajaxUrl);
					console.log('error :', formData);

					var msg = '';
					if (jqXHR.responseJSON) {
						msg = jqXHR.responseJSON.message;
					} else if (jqXHR.responseText) {
						msg = jqXHR.responseText;
					} else {
						msg = 'error';
					}
					alert(msg);
				}
			});
		}
		return dfd.promise();
	}

	/**
	 * AJAX호출 (기본 비동기)
	 * promise 사용
	 * ex) mz.util.ajaxObj(url,params).done(function(data){});
	 * 필수 인자 : ajaxUrl
	 * @param {String} ajaxUrl - 통신할 URL
	 * @param {Object} params - 전달할 파라미터 Object, 기본값은 빈 객체 {}
	 * @param {String} ajaxUrl - 통신할 httpMethod
	 * @param {Form} dataType - 통신할 dataType ex) 'json'
	 * @param {Function} callback - 콜백 함수 (선택)
	 * @param {boolean} async - 동기(true), 비동기 (false)
	 * @returns
	 */
	function ajaxObj(ajaxUrl, params, httpMethod, dataType, callback, async) {
		var dfd = $.Deferred();

		params = params ? params : new Object();

		if (!isEmpty(async)) {
			async = true;
		}

		if (isEmpty(dataType)) {
			dataType = 'json';
		}

		if (isEmpty(httpMethod)) {
			httpMethod = 'POST';
		}

		var returnObj = params;

		if (!isEmpty(ajaxUrl)) {
			$.ajax({
				type: httpMethod,
				url: ajaxUrl,
				contentType: 'application/json; charset=utf-8',
				dataType: dataType,
				data: returnObj,
				async: async,

				success: function(data) {
					if (typeof callback === 'function') {
						return callback(data);
					}

					dfd.resolve(data);
				}
				, error: function(jqXHR, textStatus, errorThrown) {
					console.log('error :', ajaxUrl);
					console.log('error :', params);

					var msg = '';
					if (jqXHR.responseJSON) {
						msg = jqXHR.responseJSON.message;
					} else if (jqXHR.responseText) {
						msg = jqXHR.responseText;
					} else {
						msg = 'error';
					}
					alert(msg);
				}
			});
		}
		return dfd.promise();
	}

	/**
	 * 전달 받은 인자 null or undefined 체크
	 * @param {Object} params
	 * @returns boolean ex) 비어있을 경우 true, 존재할 경우 false
	 */
	function isEmpty(params) {
		if (params == "" || params == null || params == undefined || params == "null" || (params != null && typeof params == "object" && !Object.keys(params).length)) {
			return true
		} else {
			return false
		}
	}

	/**
	 * Send POST
	 * form hidden 통신
	 * @param {String} url
	 * @param {String} target 필수 x -> 전송 대상이 본 페이지가 아닐경우
	 * @param {Object} params
	 * @returns
	 */
	function sendToPost(url, params, target) {
		var newForm = document.createElement('form');
		newForm.method = 'post';
		newForm.action = url;

		if (typeof target === 'undefined' || target === null || target === '') {
			target = '_self';
		};

		newForm.target = target;

		Object.entries(params).forEach(([key, value]) => {
			var hiddenTag = document.createElement('input');

			hiddenTag.type = 'hidden';
			hiddenTag.name = key;
			hiddenTag.value = value;
			newForm.appendChild(hiddenTag);
		});

		document.body.appendChild(newForm);
		newForm.submit();
		newForm.remove();
		newForm = null;
		hiddenTag = null;
	}

	/**
	 * 공백 -> 0
	 *
	 * @param {String} value
	 * @param {int} len
	 * @returns
	 */
	function prependZero(value, len) {
		while (value.toString().length < len) {
			value = "0" + value
		}
		return value;
	}

	/**
	 * 3자리 수마다 콤마(',') 표시
	 * @param {int} x
	 * @returns
	 */
	function comma(x) {
		x = replaceNull(x);
		var parts = x.toString().split(".");
		return parts[0].toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (parts[1] ? "." + parts[1] : "");
	}

	/**
	 * 콤마(',') 제거
	 * @param {String} x
	 * @returns
	 */
	function uncomma(x) {
		return x.replace(/[^\d]+/g, '');
	}

	/**
	 * null 값 공백으로 변환
	 * @param {String} value
	 * @returns
	 */
	function nullToBlank(value) {
		if (value == "null" || value == null || value == undefined) {
			return "";
		} else {
			return value;
		}
	}

	/**
	 * camelCase로 변환
	 * ex) test_string -> testString
	 * @param {String} value
	 * @param {String} targetCase 필수 x
	 * @returns
	 */
	function camelCase(value, targetCase) {
		var regex = targetCase ? new RegExp('[' + targetCase + ']+(.)?', 'g') : /[-_]+(.)?/g;
		return value.replace(regex, (match, group1) => group1 ? group1.toUpperCase() : '');
	}

	/**
	 * 타겟 문자열 replace
	 * @param {String} value
	 * @param {String} targetValue
	 * @param {String} replaceValue
	 * @returns
	 */
	function replaceValue(value, targetValue, replaceValue) {
		return value.split(targetValue).join(replaceValue);
	}

	/**
	 * email 형식 체크
	 * @param {String} value
	 * @returns
	 */
	function emailCheck(value) {
		var regex = /^[\w-\.]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
		return (value != '' && value != 'undefined' && regex.test(value));
	}

	/**
	 * form을 Object로 담아 주는 함수(serialize)
	 * @param {String} formTargetId ex) form tag Id
	 * @returns {Object} object
	 */
	function getFormData(formTargetId) {
		var formData = new Object();
		var targetForm = $("#" + formTargetId);
		var inputs = targetForm.find('input, select, textarea');

		inputs.each(function() {
			var input = $(this);
			if (input.is('input')) {
				if (input.attr('type') === 'checkbox') {
					formData[input.attr('name')] = input.prop('checked');
				} else if (input.attr('type') === 'radio') {
					if (input.prop('checked')) {
						formData[input.attr('name')] = input.val();
					}
				} else {
					formData[input.attr('name')] = input.val();
				}
			} else {
				formData[input.attr('name')] = input.val();
			}
		});

		return formData;
	}

	/**
	 * form 초기화
	 * @param {String} formTargetId
	 */
	function resetForm(formTargetId) {

		$('#' + formTargetId)[0].reset();
	}

	/**
	 * json데이터 Make Select box
	 * jsonList를 받아서 select box 옵션 생성
	 * @param {String} targetTag
	 * @param {Array} jsonData -> [{"key":"value"}]
	 * @param {boolean} baseOpt
	 * @param {boolean} dataOpt
	 * @returns
	 */
	function jsonToSelectBox(targetTag, jsonData, baseOpt, dataOpt) {

		var html = "";

		isEmpty(baseOpt) ? html += '<option value="">선택</option>' : html += '<option value="">전체</option>';

		if (isEmpty(dataOpt)) {
			dataOpt = false;
		}

		$.each(jsonData, function(index, data) {
			if (dataOpt) {
				html += '<option data-value="' + encodeURI(JSON.stringify(data)) + '" value="' + data.value + '">' + data.name + '</option>';
			} else {
				html += '<option value="' + data.value + '">' + data.name + '</option>';
			}
		});

		$('#' + targetTag).empty();
		$('#' + targetTag).append(html);

		return true;
	}

	/**
	* 코드 그룹 select box 생성
	* @param {String} targetTag
	* @param {String} groupId - 조회할 group code
	* @param {String} selVal - selected 처리할 value (선택)
	* @param {String} defaultString - 기본 option (선택)
	* @returns
	*/
	function codeToSelectBox(targetTag, groupId, selVal, defaultString) {
		var url = '/api/code-detail/' + groupId;

		mz.util.ajaxObj(url, '', 'GET', 'json',
			function callback(data) {
				if (data.status == 200) {
					var list = data.data;
					var html = '';

					$('#' + targetTag).empty();

					if (typeof defaultString !== 'undefined' && defaultString != '') {
						$('#' + targetTag).append('<option value="">' + defaultString + '</option>');
					};

					list.forEach(function(item) {
						$('#' + targetTag).append('<option value="' + item.cd + '">' + item.cdNm + '</option>');
					});

					if (typeof selVal !== 'undefined' && selVal != '') {
						$('#' + targetTag).val(selVal).prop('selected', true);
					};

				} else {
					alert('처리 중 오류가 발생하였습니다.');
					console.error(data);
				}
			}
			, false);
	}

	/**
	 * Fetch를 사용한 AJAX 호출
	 * ex) fetchObj(url, params).then(function(data){});
	 * @param {String} ajaxUrl - 통신할 URL
	 * @param {Object} params - 전달할 파라미터 Object, 기본값은 빈 객체 {}
	 * @param {String} contentType - 통신할 contentType ex) 'application/json'
	 * @param {Function} callback - 콜백 함수 (선택)
	 * @returns {Promise}
	 */
	function fetchObj(ajaxUrl, params, contentType, callback) {

		params = params ? params : new Object();

		if (contentType === undefined || contentType === null || contentType === '') {
			contentType = 'application/json' // JSON 형식
		}
		// HTTP 옵션
		var fetchOptions = {
			method: 'PATCH', // 요청 메소드
			headers: {
				// 'Content-Type' : 'multipart/form-data'  // formData형식
				'Content-Type': contentType
			},
			body: JSON.stringify(params) // 파라미터를 JSON 문자열로 변환
		};

		// fetch 통신
		return fetch(ajaxUrl, fetchOptions)
			.then(response => {
				if (!response.ok) {
					throw new Error('Network Error');
				}
				return response.json(); // JSON 파싱
			})
			.then(data => {
				if (typeof callback === 'function') {
					callback(data);
				}
				return data;
			})
			.catch(error => {
				console.error('Fetch Error:', error);
			});
	}

	/**
	 * HTML 요소에 CSS 클래스를 추가하는 함수
	 * ex) mz.util.addClass('#temp', 'hide');
	 * @param {String} element
	 * @param {String} className
	 */
	function addClass(element, className) {
		if ($(element).hasClass(className) === false) {
			$(element).addClass(className);
		}
	}

	/**
	 * HTML 요소에 CSS 클래스를 제거하는 함수
	 * * ex) mz.util.removeClass('#temp', 'hide');
	 * @param {String} element
	 * @param {String} className
	 */
	function removeClass(element, className) {
		$(element).removeClass(className);
	}

	/**
	 * 쿠기 값 추가
	 * @param {String} name
	 * @param {String} value
	 * @param {String} path
	 * @param {Date} expiredays
	 */
	function addCookie(name, value, path, expiredays) {
		var expiryDate = new Date();
		// 만료일이 지정된 경우, 해당 일수를 더함
		if (expiredays) {
			expiryDate.setDate(expiryDate.getDate() + expiredays);
		}
		// 쿠키 문자열 구성
		var cookieValue = encodeURIComponent(name) + "=" + encodeURIComponent(value) +
			"; path=" + (path ? path : "/") + // 경로 기본값은 '/'
			(expiredays ? "; expires=" + expiryDate.toUTCString() : ""); // 만료일이 있는 경우만 추가
		document.cookie = cookieValue;
	}

	/**
	 * 쿠키 제거
	 * @param {String} name
	 */
	function removeCookie(name) {
		document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	}

	/**
	 * 쿠기 값 가져오기
	 * @param {String} name
	 * @returns
	 */
	function getCookie(name) {
		var nameEQ = encodeURIComponent(name) + "=";
		var ca = document.cookie.split(';'); // 쿠키를 ';'로 분리하여 배열 생성
		for (var i = 0; i < ca.length; i++) {
			var c = ca[i];
			while (c.charAt(0) === ' ') c = c.substring(1, c.length); // 앞 공백 제거
			if (c.indexOf(nameEQ) === 0) return decodeURIComponent(c.substring(nameEQ.length, c.length));
		}
		return null; // 쿠키가 없는 경우 null 반환
	}

	/**
	 * 주민등록번호 패턴 검사
	 * @param {String} str
	 * @returns
	 */
	function isValidIdNumber(str) {
		var format = /[0-9]{6}[-. ][1-4][0-9]{6}/;
		return format.test(str);
	}

	/**
	 * 문자열에 한글 문자가 하나라도 있는지 검사
	 * @param {String} value
	 * @returns
	 */
	function hasKoreanCharacter(value) {
		var format = /[ㄱ-힣]/;
		return format.test(value);
	}

	/**
	 * 영문 대소문자와 숫자로만 구성된 문자열 패턴 검사
	 * @param {String} value
	 * @returns
	 */
	function isAlphanumeric(value) {
		var format = /^[A-Za-z0-9]+$/;
		return format.test(value);
	}

	/**
	 * 전화번호 유효성 검사
	 * @param {String} value
	 * @returns
	 */
	function isValidPhoneNumber(value) {
		var format = /^\d{2,3}-\d{3,4}-\d{4}$/;
		return format.test(value);
	}

	/**
	 * 휴대전화번호 유효성 검사
	 * @param {String} value
	 * @returns
	 */
	function isValidMobileNumber(value) {
		var format = /^(010)[0-9]{4}[0-9]{4}$/;
		return format.test(value);
	}

	/**
	 * 테이블 데이터 가져오기
	 * @param {String} formId
	 * @returns
	 */
	function getResultList(formId) {
		var listForm = $('#' + formId);
		var resultData = null;

		ajaxObj(listForm.attr('action'), listForm.serialize(), 'GET', '', '', false).done(function(data) {
			resultData = data;
		});

		return resultData;
	}
	/**
	 * 데이터를 페이징,출력할 목록 처리
	 * @param {String} formId
	 * @param {String} vIndex
	 */
	function drawResultList(formId, vIndex) {
		console.log(vIndex);
		if (!isEmpty(vIndex)) {
			$('#page').val(vIndex);
		}
		var data = getResultList(formId);
		mz.util.drawListTable(data);
		drawPaging(data, formId);

	}

	/**
	 * 페이징 계산
	 * @param {int} page
	 * @param {int} pageScale
	 * @param {int} totalCount
	 * @returns
	 */
	function calculatePagination(page, pageScale, totalCount) {
		var totalPages = Math.ceil(totalCount / pageScale);
		var currentPage = parseInt(page);
		var firstPageOnPageList = Math.floor((currentPage - 1) / pageScale) * pageScale + 1;
		var lastPageOnPageList = Math.min(firstPageOnPageList + pageScale - 1, totalPages);
		var previousPage = Math.max(firstPageOnPageList - 1, 1);
		var nextPage = Math.min(lastPageOnPageList + 1, totalPages);

		return {
			totalPages: totalPages,
			firstPageOnPageList: firstPageOnPageList,
			lastPageOnPageList: lastPageOnPageList,
			previousPage: previousPage,
			nextPage: nextPage
		};
	}

	/**
	 * 페이징 그리기
	 * @param {Object} data
	 */
	function drawPaging(data, formId) {
		$('#pagingUl').empty();

		if (data.data && !isEmpty(data.data.content)) {
			var paginationData = calculatePagination(data.data.number + 1, data.data.size, data.data.totalElements); // 페이지 번호를 1 기반으로 조정

			var first = paginationData.firstPageOnPageList;
			var last = paginationData.lastPageOnPageList;
			var totalPages = paginationData.totalPages;
			var previous = paginationData.previousPage;
			var next = paginationData.nextPage;

			if (totalPages > 0) {
				previous = previous < 1 ? 1 : previous;
				next = data.data.number + 1 === totalPages ? totalPages : next;

				var pagingHtml = '';
				pagingHtml += '<li class="pagination_item"><a href="javascript:void(0)" onclick="mz.util.drawResultList(\'' + formId + '\', 1);"><i class="icon_pagination_first"></i></a></li>';
				pagingHtml += '<li class="pagination_item"><a href="javascript:void(0)" onclick="mz.util.drawResultList(\'' + formId + '\',' + previous + ');"><i class="icon_pagination_prev"></i></a></li>';

				for (var idx = first; idx <= last; idx++) {
					if (idx === data.data.number + 1) {
						pagingHtml += '<li class="pagination_item"><a href="javascript:void(0)" onclick="mz.util.drawResultList(\'' + formId + '\',' + idx + ');" class="pagination--active">' + idx + '</a></li>';
					} else {
						pagingHtml += '<li class="pagination_item"><a href="javascript:void(0)" onclick="mz.util.drawResultList(\'' + formId + '\',' + idx + ');">' + idx + '</a></li>';
					}
				}

				pagingHtml += '<li class="pagination_item"><a href="javascript:void(0)" onclick="mz.util.drawResultList(\'' + formId + '\',' + next + ');"><i class="icon_pagination_next"></i></a></li>';
				pagingHtml += '<li class="pagination_item"><a href="javascript:void(0)" onclick="mz.util.drawResultList(\'' + formId + '\',' + totalPages + ');"><i class="icon_pagination_last"></i></a></li>';

				$('#pagingUl').append(pagingHtml);
			}

			$('#page').val(data.data.number + 1);

			// 페이지 uri 강제 변경
			const urlParams = new URLSearchParams(window.location.search);
			urlParams.set('page', data.data.number + 1); // 현재 페이지 설정
			history.pushState(null, '', `?${urlParams.toString()}`); // URL 업데이트
		} else {
			$('#pagingUl').empty();
		}
	}

	/**
	 * 로그인 성공시 접속한 사용자 이름
	 */
	function getUserNm() {
		var userNm = decodeURI(window.localStorage.getItem("userNm"));

		return userNm;
	}

	/**
	 * 입력키 제어 (한글, 숫자 등)
	 * @param {int} keyType
	 * @param {Obj} obj
	 */
	function inputKeyType(keyType, obj) {

		if (keyType == 1) { //숫자만
			obj.value = obj.value.replace(/[^0-9]/g, '');
		}

		if (keyType == 2) { //한글만
			obj.value = obj.value.replace(/[^ㄱ-ㅎ|ㅏ-ㅣ|가-힣|\s]/g, '');
		}

		if (keyType == 3) { //한글입력 불가능
			obj.value = obj.value.replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣|\s]/g, '');
		}

		if (keyType == 4) { //숫자만(소수점 포함)
			obj.value = obj.value.replace(/[^0-9.]/g, '');
		}
	}

	/**
	 * 해당 input enter 실행 적용 및 함수 실행
	 * @param {String} targetInput
	 * @param {String} callback - callback 함수
	 */
	function onEnterKey(targetInput, callFunc) {
		// 버튼 keyup event
		$('#' + targetInput).on('keyup', function() {
			if (window.event.keyCode == 13) {
				callFunc();
			}
		});
	}

	/**
	 * required 클래스를 가진 모든 input 빈값 체크
	 * (input 태그 내 title 속성 지정 필요)
	 *
	 * @returns {Boolean}
	 */
	function checkReqInput() {

		let item;
		let checkFlag = Boolean(true);

		$('.required').each(function() {

			if ($(this).val().trim() == '') {
				item = this;
			}
			
			if (typeof item != 'undefined') {
				alert($(item).attr('title') + '은(는) 필수 입력 항목입니다.');

				if ($(item).attr('type') == 'hidden') {
					// type="hidden"인 경우, 연결된 라벨로 포커스를 이동
					let label = $('label[for="' + $(item).attr('id') + '"]');
					if (label.length) {
						label.focus(); // 라벨에 포커스 주기
						label.get(0).scrollIntoView({ behavior: 'smooth', block: 'center' });
					}
				} else {
					$(item).focus();
				}
				checkFlag = Boolean(false);
				return checkFlag;
			}
		});
		return checkFlag;
	}

	/**
	 * 오늘 날짜로부터 날짜 계산 (일/주/월/연 단위)
	 * @param {String} calType - DAY / WEEK / MONTH / YEAR
	 * @param {integer} num - 계산할 숫자
	 * @returns
	 */
	function calculateDateFromToday(calType, num) {

		let today = new Date();
		calType = calType.toUpperCase();

		let day = today.getDate();
		let month = today.getMonth();
		let year = today.getFullYear();

		// 일 단위 (DAY)
		let setDay = new Date(today.setDate(day + num)).toLocaleDateString("ko-KR", {
			year: "numeric",
			month: "2-digit",
			day: "2-digit"
		})

		// 주 단위 (WEEK)
		let setWeek = new Date(today.setDate(day + (num * 7))).toLocaleDateString("ko-KR", {
			year: "numeric",
			month: "2-digit",
			day: "2-digit"
		})

		// 달 단위 (MONTH)
		let setMonth = new Date(today.setMonth(month + num)).toLocaleDateString("ko-KR", {
			year: "numeric",
			month: "2-digit",
			day: "2-digit"
		})

		// 연 단위(YEAR)
		let setYear = new Date(today.setYear(year + num)).toLocaleDateString("ko-KR", {
			year: "numeric",
			month: "2-digit",
			day: "2-digit"
		})

		let target;

		if (calType === 'DAY') {
			target = setDay;
		} else if (calType === 'WEEK') {
			target = setWeek;
		} else if (calType === 'MONTH') {
			target = setMonth;
		} else if (calType === 'YEAR') {
			target = setYear;
		} else {
			return;
		}

		let result = target.replaceAll(/(\s*)/g, '').split('.');
		result = result.filter((el, i) => el !== '').join('-');

		return result;

	}

	/**
	 * 목록 엑셀 다운로드
	 * @param {String} url
	 * @param {String} formId
	 * @returns
	 */
	function downloadListToExcel(url, formId) {

		// paging 해제 (DTO field)
		let input = document.createElement('input');
		input.setAttribute("type", "hidden");
		input.setAttribute("name", "pagingYn");
		input.setAttribute("id", "pagingYn");
		input.setAttribute("value", "N");

		formId.append(input);

		$.ajax({
			url: url,
			type: 'GET',
			data: $(formId).serialize(),
			xhrFields: {
				responseType: 'blob',
			},
			success: function(data, status, xhr) {

				if (status == 'success') {
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
			error: function(xhr, textStatus, errorThrown) {
				alert('파일을 가져오는 데 실패했습니다.');
			},
			complete: function() {
				if ($('#pagingYn').length > 0) {
					$('#pagingYn').remove();
				}
			}
		});
	}

	/**
	 * init 설정
	 *
	 */
	function init() {
		$('.datepicker').datepicker({
			dateFormat: 'yy-mm-dd'
			, autoclose: true
			, language: 'ko'
			, todayBtn: 'linked'
			, monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']
			, monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']
			, dayNames: ['일', '월', '화', '수', '목', '금', '토']
			, dayNamesShort: ['일', '월', '화', '수', '목', '금', '토']
			, dayNamesMin: ['일', '월', '화', '수', '목', '금', '토']
		});
	}


	return {
		ajaxObj: ajaxObj,
		ajaxForm: ajaxForm,
		isEmpty: isEmpty,
		sendToPost: sendToPost,
		prependZero: prependZero,
		comma: comma,
		uncomma: uncomma,
		nullToBlank: nullToBlank,
		camelCase: camelCase,
		replaceValue: replaceValue,
		emailCheck: emailCheck,
		getFormData: getFormData,
		resetForm: resetForm,
		jsonToSelectBox: jsonToSelectBox,
		codeToSelectBox: codeToSelectBox,
		fetchObj: fetchObj,
		addClass: addClass,
		removeClass: removeClass,
		addCookie: addCookie,
		removeCookie: removeCookie,
		getCookie: getCookie,
		isValidIdNumber: isValidIdNumber,
		hasKoreanCharacter: hasKoreanCharacter,
		isAlphanumeric: isAlphanumeric,
		isValidPhoneNumber: isValidPhoneNumber,
		isValidMobileNumber: isValidMobileNumber,
		getResultList: getResultList,
		drawResultList: drawResultList,
		calculatePagination: calculatePagination,
		drawPaging: drawPaging,
		getUserNm: getUserNm,
		inputKeyType: inputKeyType,
		onEnterKey: onEnterKey,
		checkReqInput: checkReqInput,
		calculateDateFromToday: calculateDateFromToday,
		downloadListToExcel: downloadListToExcel,
		init: init,
	};

})();
