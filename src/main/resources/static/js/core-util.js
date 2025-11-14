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
 */

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
     function ajaxForm(ajaxUrl, formParams, httpMethod, dataType , callback, async) {
        var dfd = $.Deferred();

        if (async === undefined || typeof async !== 'boolean') {
            async = true;
        }

        if (dataType === undefined || dataType === null || dataType === '') {
            dataType = 'json';
        }

        if(httpMethod === undefined || httpMethod === null || httpMethod === ''){
           httpMethod = 'POST';
        }

        if (ajaxUrl !== null && ajaxUrl !== '') {

            var formData = new FormData($(formParams)[0]); // 폼 FormData로 변환

            $.ajax({
                type: 'POST',
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
                    console.log('error :', params);
                    var msg = '';

                    if (jqXHR.responseJSON && jqXHR.responseJSON.message) {
                        msg = jqXHR.responseJSON.message;
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
    function ajaxObj( ajaxUrl, params, httpMethod, dataType, callback, async ){
        var dfd = $.Deferred();

        params = params ? params : new Object();

        if( async === undefined || typeof async !== 'boolean' ){
            async = true;
        }

        if(httpMethod === undefined || httpMethod === null || httpMethod === ''){
           httpMethod = 'POST';
        }

        if (dataType === undefined || dataType === null || dataType === '') {
            dataType = 'json';
        }

        var returnObj = params;

        if( ajaxUrl !== null && ajaxUrl !== '' ){
            $.ajax({
                type: httpMethod,
                url: ajaxUrl,
                contentType: 'application/json; charset=utf-8',
                dataType : dataType,
                data: returnObj,
                async : async,

                success : function(data){
                    if( typeof callback === 'function' ){
                        return callback( data );
                    }

                    dfd.resolve(data);
                }
                , error : function(jqXHR, textStatus, errorThrown){
                    console.log('error :', ajaxUrl);
                    console.log('error :', params);
                    var msg = '';

                    if (jqXHR.responseJSON && jqXHR.responseJSON.message) {
                        msg = jqXHR.responseJSON.message;
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
    function isEmpty(params){
        if (params == "" || params == null || params == undefined || params =="null" || (params != null && typeof params == "object" && !Object.keys(params).length)) {
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
    function sendToPost( url, params, target ){
        var newForm = document.createElement('form');
        newForm.method = 'post';
        newForm.action = url;

        if(typeof target === 'undefined' || target === null || target === '' ){
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
    function nullToBlank(value){
        if (value == "null" || value == null || value == undefined ) {
                return "" ;
        } else {
            return value ;
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

        var regex=/([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
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
    function resetForm(formTargetId){

        $('#'+ formTargetId)[0].reset();
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
    function jsonToSelectBox( targetTag, jsonData , baseOpt, dataOpt){

        var html = "";

        isEmpty(baseOpt) ? html += '<option value="">선택</option>' : html += '<option value="">전체</option>';

        if(isEmpty(dataOpt)){
            dataOpt = false;
        }

        $.each( jsonData ,function(index,data){
            if(dataOpt){
                html += '<option data-value="'+ encodeURI( JSON.stringify(data) ) + '" value="'+data.value+'">'+data.name+'</option>';
            } else {
                html += '<option value="'+ data.value +'">'+ data.name +'</option>';
            }
        });

        $('#' + targetTag).empty();
        $('#' + targetTag).append(html);

        return true;
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
            method: 'POST', // 요청 메소드
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
        for(var i=0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) === ' ') c = c.substring(1,c.length); // 앞 공백 제거
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
        var format = /^\d{3}-\d{3,4}-\d{4}$/;
        return format.test(value);
    }

    /**
     * 테이블 데이터 가져오기
     * @param {String} formId
     * @returns
     */
    function getResultList(formId){
        var listForm = $('#'+formId);

        var resultData  =   null ;
        ajaxObj( listForm.attr('action'), listForm.serializeArray(),'GET','','',false).done(function(data){
            resultData = data;
        });

        return resultData;
    }
    /**
     * 데이터를 페이징,출력할 목록 처리
     * @param {String} formId
     * @param {String} vIndex
     */
    function drawResultList(formId, vIndex){

        if(!isEmpty(vIndex)){
            $('#page').val(vIndex);
        }
        var data = getResultList(formId);
        if( !isEmpty(data.data) ){
            mz.util.drawListTable(data);
            drawPaging(data,formId);
        } else {
            mz.util.drawListTable(new Array());
            drawPaging(new Object(),formId);
        }

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
    function drawPaging(data,formId){
        $('#pagingUl').empty();
        if( !isEmpty(data.data) ){
            var paginationData = calculatePagination(data.data.page, data.data.pageScale, data.data.totalCount);

            var first           = paginationData.firstPageOnPageList;
            var last            = paginationData.lastPageOnPageList ;
            var previous        = first - 1;
            var next            = last + 1;

            if( data.data.totalCount > 0 ) {
                if( previous < 1 ){
                    previous = 1;
                }
                if( next > last ){
                    next = last;
                }

                var pagingHtml = '';
                    pagingHtml += '<li class="pagination_item"><a href="#" onclick="mz.util.drawResultList(\'' + formId + '\',' + first + ');"><i class="icon_pagination_first"></i></a></li>';
                    pagingHtml += '<li class="pagination_item"><a href="#" onclick="mz.util.drawResultList(\'' + formId + '\',' + previous +');"><i class="icon_pagination_prev"></i></a></li>';

                for( var idx = first; idx <= last; idx++ ){
                    if( idx == data.data.page ){
                        pagingHtml += '<li class="pagination_item"><a href="#" onclick="mz.util.drawResultList(\'' + formId + '\',' + idx +');" class="pagination--active">'+ idx +'</a></li>';
                    } else {
                        pagingHtml += '<li class="pagination_item"><a href="#" onclick="mz.util.drawResultList(\'' + formId + '\',' + idx +');">'+ idx +'</a></li>';
                    }
                }

                pagingHtml += '<li class="pagination_item"><a href="#" onclick="mz.util.drawResultList(\'' + formId + '\',' + next +');"><i class="icon_pagination_next"></i></a></li>';
                pagingHtml += '<li class="pagination_item"><a href="#" onclick="mz.util.drawResultList(\'' + formId + '\',' + last +');"><i class="icon_pagination_last"></i></a></li>';

                $('#pagingUl').append(pagingHtml);
            }

            $('#page').val(data.data.page);
        } else {
            $('#pagingUl').empty();
        }
    }

    return {
        ajaxObj : ajaxObj,
        ajaxForm : ajaxForm,
        isEmpty : isEmpty,
        sendToPost : sendToPost,
        prependZero : prependZero,
        comma : comma,
        uncomma : uncomma,
        nullToBlank : nullToBlank,
        camelCase : camelCase,
        replaceValue : replaceValue,
        emailCheck : emailCheck,
        getFormData : getFormData,
        resetForm : resetForm,
        jsonToSelectBox : jsonToSelectBox,
        fetchObj : fetchObj,
        addClass : addClass,
        removeClass : removeClass,
        addCookie : addCookie,
        removeCookie : removeCookie,
        getCookie : getCookie,
        isValidIdNumber : isValidIdNumber,
        hasKoreanCharacter : hasKoreanCharacter,
        isAlphanumeric : isAlphanumeric,
        isValidPhoneNumber : isValidPhoneNumber,
        isValidMobileNumber : isValidMobileNumber,
        getResultList : getResultList,
        drawResultList : drawResultList,
        calculatePagination : calculatePagination,
        drawPaging : drawPaging,

    };

})();
