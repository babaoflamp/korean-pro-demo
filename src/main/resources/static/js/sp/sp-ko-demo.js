/**
 *  AI 한국어학습 (speechpro-ko)
 */

const audio = new Audio();

let questionBox = $(".question_detach").detach();
let resultBox = $(".result_detach").detach();
let phonesBox = $(".detail_detach").find(".phones_detach").detach();
let resultDetailBox = $(".detail_detach").detach();

let isAllComplete = true; // 모두 제출 여부
let loadQuestion; // 진입 시 세팅할 문제
let isPlaying = false; // 현재 오디오 재생중 여부

$(document).ready(function () {
  // 사용자 이름 확인 및 팝업 표시
  if (!sessionStorage.getItem("userName")) {
    openModal("name-input-popup");
  }

  // 이름 확인 버튼 클릭
  $("#confirmName").on("click", function () {
    let name = $("#userName").val().trim();
    if (name) {
      sessionStorage.setItem("userName", name);
      $(".name-input-popup").hide();
    } else {
      $("#alertText").text("이름을 입력해주세요.");
      $(".name-input-popup").hide();
      openModal("alert-popup");
      setTimeout(function () {
        $(".alert-popup").hide();
        openModal("name-input-popup");
      }, 1500);
    }
  });

  // 문제 목록
  fn_getQuestionList();

  // 문제 목록 - 문제 클릭 시
  $(".question_detach").on("click", function () {
    let koId = $(this).data("ko-id");
    recorder = null;
    evaluateData = null;

    fn_getQuestion(koId);
  });

  // 이전 버튼 클릭 시
  $("#prev").on("click", function () {
    moveToPrevious();
  });

  // 다음 버튼 클릭 시
  $("#next").on("click", function () {
    moveToNext();
  });

  // 제출하기
  $("#evaluate").on("click", function () {
    if (evaluateData === undefined || evaluateData == null) {
      $("#alertText").text("녹음을 완료해주세요.");
      openModal("alert-popup");
    } else {
      fn_evaluate();
    }
  });

  // 지난 결과 확인
  $("#previous").on("click", function () {
    fn_getPreviousResult();
  });

  // 지난 결과 확인 > 단어 클릭 시
  $("#resultTarget").on("click", "li", function () {
    let wordIdx = $(this).data("word-idx");
    fn_setResultDetail(wordData[wordIdx]);
  });

  // 다시 듣기 클릭 시
  $("#replay").on("click", function () {
    fn_getFile();
  });

  // 다시풀기 클릭 시 녹음 삭제
  $("#retry").on("click", function () {
    recorder = null;
    evaluateData = null;
  });

  // 결과창 다시풀기, x 버튼 클릭 시 오디오 정지
  $(".learning-popup .close-btn").on("click", function () {
    audio.pause();
    isPlaying = false;
  });

  // modal - 결과창 다음 문제 바로가기
  $("#popNext").on("click", function () {
    $(".learning-popup").hide();
    moveToNext();
  });

  // 전체 결과 보기 버튼 클릭
  $("#viewAllResults").on("click", function () {
    fn_showAllResults();
  });

  // Excel 다운로드 버튼 클릭
  $("#exportExcel").on("click", function () {
    fn_exportExcel();
  });
});

// 문제 목록
function fn_getQuestionList() {
  mz.util.ajaxObj(
    "/api/sp/demo/question",
    "",
    "GET",
    "json",
    fn_getQuestionList_callback,
    false
  );
}

// 문제 목록 callback
function fn_getQuestionList_callback(result) {
  $("#questionTarget").empty();

  if (result.status == 200) {
    let data = result.data;

    data.forEach(function (item) {
      let box = questionBox.clone();
      $(box).attr("data-ko-id", item.koId);
      $(box).find(".order").text(item.order);
      $(box).find(".sentence").text(replaceSpace(item.sentence));
      $("#questionTarget").append(box);
    });

    if (loadQuestion === undefined) loadQuestion = data[0].koId;

    fn_getQuestion(loadQuestion);
    fn_paging();
  } else {
    $("#alertText").text("오류가 발생하였습니다.");
    openModal("alert-popup");
  }
}

// 문제 정보 1개
function fn_getQuestion(koId) {
  mz.util.ajaxObj(
    "/api/sp/demo/question/" + koId,
    "",
    "GET",
    "json",
    fn_getQuestion_callback,
    false
  );
}

// 문제 정보 1개 callback
function fn_getQuestion_callback(result) {
  if (result.status == 200) {
    let data = result.data;

    $(".question_detach").siblings().removeClass("active");
    $(".question_detach").each(function (idx, item) {
      if ($(this).data("ko-id") == data.koId) $(this).addClass("active");
    });

    $("#detail_order").text(data.order);
    $("#detail_sentence").text(replaceSpace(data.sentence));
    $("#detail_sentence").attr("data-detail-ko-id", data.koId);

    // 결과 확인창 다음 문제 버튼
    $("#popNext").show();
    if ($(".question_detach:last-child").attr("data-ko-id") == data.koId) {
      $("#popNext").hide();
    }

    fn_paging();
  } else {
    $("#alertText").text("오류가 발생하였습니다.");
    openModal("alert-popup");
  }

  fn_resetBadges(false);
  fn_resetMicBtns(false);
}

// 마이크 관련 버튼 초기화
function fn_resetMicBtns(hasAnswered) {
  // 제출 - 재녹음
  if (hasAnswered) {
    fn_setRerecordingBtn();

    // 미제출 - 초기 마이크
  } else {
    $("#arrorTxt").html(preTxt);
    $("#mic").prop("hidden", false);
    $("#recording").prop("hidden", true);

    $("#stopRecord").removeAttr("style");
    $("#stopRecord").addClass("disabled");
    $("#reRecord").attr("style", "display:none;");

    $("#tooltip").prop("hidden", true);
  }
}

// 목록, 본문 뱃지 초기화
function fn_resetBadges(hasAnswered) {
  // st~ class 삭제
  $("#detail_badge").removeClass(function (_, c) {
    return c.split(" ").find((cls) => cls.startsWith("st"));
  });
  $("#detail_badge").addClass(hasAnswered === true ? "st01" : "st02");
  $("#detail_badge").text(hasAnswered === true ? "제출완료" : "미제출");

  let listBadge = $(".question_detach.active").find(".badges");
  listBadge.removeClass(function (_, c) {
    return c.split(" ").find((cls) => cls.startsWith("st"));
  });

  // 목록 뱃지 update
  listBadge.addClass(hasAnswered === true ? "st01" : "st02");
  listBadge.text(hasAnswered === true ? "제출완료" : "미제출");
}

// 지난 결과
function fn_getPreviousResult() {
  let koId = $("#detail_sentence").attr("data-detail-ko-id");

  // 지난 결과
  mz.util.ajaxObj(
    "/api/sp/demo/answer/" + koId + "/pre",
    "",
    "GET",
    "json",
    fn_getPreviousResult_callback,
    false
  );
}

// 지난 결과 callback
function fn_getPreviousResult_callback(result) {
  if (result.status === 200) {
    fn_setResult(result.data);
    openModal("learning-popup");
  } else if (result.status === 404) {
    // 지난결과 없음
    $("#alertText").text("지난 결과가 없습니다.");
    openModal("alert-popup");
  } else {
    $("#alertText").text("오류가 발생했습니다.");
    openModal("alert-popup");
  }
}

// 발음평가
function fn_evaluate() {
  openModal("loading-popup");

  let data = {
    id: $("#detail_sentence").attr("data-detail-ko-id"),
    "wav usr": evaluateData,
  };
  setTimeout(function () {
    mz.util.ajaxObj(
      "/api/sp/demo/evaluate",
      JSON.stringify(data),
      "POST",
      "json",
      fn_evaluate_callback,
      false
    );
  }, 500);
}

// 발음 평가 callback
function fn_evaluate_callback(result) {
  $(".loading-popup").hide();

  if (result.status == 200) {
    // answerId를 sessionStorage에 저장
    if (result.data && result.data.answerId) {
      let answerIds = JSON.parse(sessionStorage.getItem("answerIds") || "[]");
      if (!answerIds.includes(result.data.answerId)) {
        answerIds.push(result.data.answerId);
        sessionStorage.setItem("answerIds", JSON.stringify(answerIds));
      }
    }

    openModal("checkResults-popup");
    $("#evaluateConfirm").click(function () {
      $(".checkResults-popup").css("display", "none");

      fn_setResult(result.data);
      fn_getQuestion($("#detail_sentence").attr("data-detail-ko-id"));

      openModal("learning-popup");
    });

    // 녹음 재진행
  } else {
    openModal("recording-popup");

    if ($("#detail_badge").hasClass("st01")) {
      fn_resetMicBtns(true);
    } else {
      fn_resetMicBtns(false);
    }
  }
}

let wordData; // 클릭 시 상세점수 타켓 세팅을 위해 데이터 저장

// 학습 결과 세팅
function fn_setResult(data) {
  $("#resultTarget").empty();
  $("#resultDetailTarget").empty();

  $("#resultTarget").attr("data-file-seq", data.fileSeq);

  // 파랑 step03: 70~100점 / 녹색 step02 : 50~69점 / 노랑 step01 : 0~49점
  let totalScore = Math.round(data.result.result.quality.sentences[1].score);
  $("#totalScore").html(totalScore);

  let submitDate = data.submittedAt;
  let formatDate = fn_getDate(submitDate);

  $("#date").html(formatDate);

  let words = data.result.result.quality.sentences[1].words;
  wordData = words;

  words.forEach(function (item, idx) {
    if (item.text !== "!SIL") {
      // 휴지구간 아닌것만
      let box = resultBox.clone();
      let score = Math.round(item.score);
      let level = fn_getLevelClass(score);

      $(box).addClass(level);
      $(box).attr("data-word-idx", idx);
      $(box).find(".learning_text").text(item.text);
      $(box).find(".learning_score").text(score);
      $("#resultTarget").append(box);
    }
  });

  // 상세 점수
  fn_setResultDetail(wordData[0]);

  // 전체 결과 데이터에 이 평가 결과를 추가
  fn_saveResultToSessionStorage(data);
}

// 평가 결과를 sessionStorage에 저장
function fn_saveResultToSessionStorage(data) {
  // 현재 문제의 order 찾기
  let activeQuestion = $(".question_detach.active");
  let order = activeQuestion.find(".order").text();
  let sentence = activeQuestion.find(".sentence").text();

  // 현재 평가 점수
  let sentenceScore = Math.round(data.result.result.quality.sentences[1].score);

  // 단어 점수들을 배열로 만들기
  let wordScores = [];
  data.result.result.quality.sentences[1].words.forEach(function (word) {
    if (word.text !== "!SIL") {
      wordScores.push(Math.round(word.score));
    }
  });
  let wordScoresStr = wordScores.join(", ");

  // 기존 결과 데이터 로드
  let allResultsData = JSON.parse(
    sessionStorage.getItem("allResultsData") || "[]"
  );

  // 같은 order의 결과가 있으면 업데이트, 없으면 추가
  let existingIndex = allResultsData.findIndex((item) => item.order == order);
  if (existingIndex >= 0) {
    allResultsData[existingIndex] = {
      order: order,
      sentence: sentence,
      sentenceScore: sentenceScore,
      wordScores: wordScoresStr,
    };
  } else {
    allResultsData.push({
      order: order,
      sentence: sentence,
      sentenceScore: sentenceScore,
      wordScores: wordScoresStr,
    });
  }

  sessionStorage.setItem("allResultsData", JSON.stringify(allResultsData));
}

// 결과 상세 점수
function fn_setResultDetail(wordData) {
  $("#resultDetailTarget").empty();

  wordData.syll.forEach(function (item, idx) {
    let detailDetach = resultDetailBox.clone();
    // 음절
    let syllScore = Math.round(item.score);
    let syllLevel = fn_getLevelClass(syllScore);

    detailDetach.find(".circle-txt .txt").text("[" + item.text + "]");
    detailDetach.find(".circle-txt .circle").addClass(syllLevel);
    detailDetach.find(".circle > p").text(syllScore);
    detailDetach.find("ul").addClass(`phones_${idx}`);

    $("#resultDetailTarget").append(detailDetach);

    // 음소
    item.phones.forEach(function (phone) {
      let phonesDetach = phonesBox.clone();
      let phoneScore = Math.round(phone.score);
      let phoneLevel = fn_getLevelClass(phoneScore);

      phonesDetach.find(".box").addClass(phoneLevel);
      phonesDetach.find(".box .txt").html(phone.symbol);
      phonesDetach.find(".box .num").html(phoneScore);

      $(`.phones_${idx}`).append(phonesDetach);
    });
  });
}

// scrore level
function fn_getLevelClass(score) {
  let level;
  if (score >= 70 && score <= 100) {
    level = "step03";
  } else if (score >= 50 && score <= 69) {
    level = "step02";
  } else {
    level = "step01";
  }
  return level;
}

// 다시 듣기 음성 파일 요청
function fn_getFile() {
  if (!isPlaying) {
    let seq = $("#resultTarget").attr("data-file-seq");
    let data = {
      fileSeq: seq,
    };
    mz.util.ajaxObj(
      "/api/sp/demo/files",
      data,
      "GET",
      "json",
      fn_getFile_callback,
      false
    );
  }
}

function fn_getFile_callback(result) {
  let dtlSeq = result.data[0].fileDtlSeq;

  $.ajax({
    url: "/api/sp/demo/file/" + dtlSeq,
    type: "GET",
    xhrFields: {
      responseType: "blob",
    },
    success: function (data, status, xhr) {
      if (status == "success") {
        // 이미 재생 중이면 새로 시작하지 않음
        if (isPlaying) {
          return;
        }

        let audioURL = URL.createObjectURL(data);
        audio.src = audioURL;
        audio.play();
        isPlaying = true;
      } else {
        $("#alertText").text("오류가 발생하였습니다.");
        openModal("alert-popup");
      }
    },
  });
}

audio.pause = function () {
  isPlaying = false;
  audio.currentTime = 0;
  audio.src = "";
};

audio.onended = function () {
  isPlaying = false;
  audio.src = "";
};

// 이전, 다음 단계 버튼 처리
function fn_paging() {
  let questions = $("#questionTarget .question_detach");
  let activeQuestion = questions.filter(".active");

  $("#prev").removeClass("disabled");
  $("#next").removeClass("disabled");

  // 첫 번째 요소일 경우 이전 버튼 비활성화
  if (questions.first().is(activeQuestion)) {
    $("#prev").addClass("disabled");
    $("#next").removeClass("disabled");
  }
  // 마지막 요소일 경우 다음 버튼 비활성화
  if (questions.last().is(activeQuestion)) {
    $("#prev").removeClass("disabled");
    $("#next").addClass("disabled");
  }
}

// 버튼 클릭 시 이전/다음 요소로 이동
function moveToPrevious() {
  if ($("#prev").hasClass("disabled")) {
    return;
  }
  let activeQuestion = $("#questionTarget .question_detach").filter(".active");
  let prevQuestion = activeQuestion.prev(".question_detach");

  if (prevQuestion.length) {
    fn_getQuestion(prevQuestion.data("ko-id"));
  }
  fn_resetBadges(false);
  fn_resetMicBtns(false);
}

function moveToNext() {
  if ($("#next").hasClass("disabled")) {
    return;
  }

  let activeQuestion = $("#questionTarget .question_detach").filter(".active");
  let nextQuestion = activeQuestion.next(".question_detach");

  if (nextQuestion.length) {
    fn_getQuestion(nextQuestion.data("ko-id"));
  }
  fn_resetBadges(false);
  fn_resetMicBtns(false);
}

// 날짜 세팅
function fn_getDate(dateStr) {
  const date = new Date(dateStr);
  const formattedDate = date.toLocaleDateString("ko-KR", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
  return formattedDate;
}

// &nbsp 대체
function replaceSpace(sentence) {
  return sentence.replaceAll(/\u00A0/g, " ");
}

// 전체 결과 보기 함수
function fn_showAllResults() {
  let userName = sessionStorage.getItem("userName");
  let answerIds = JSON.parse(sessionStorage.getItem("answerIds") || "[]");

  if (!userName) {
    $("#alertText").text("사용자 이름이 설정되지 않았습니다.");
    openModal("alert-popup");
    return;
  }

  if (answerIds.length === 0) {
    $("#alertText").text("저장된 평가 결과가 없습니다.");
    openModal("alert-popup");
    return;
  }

  // 사용자명 표시
  $("#allResultUserName").text(userName);

  // 테이블 바디 초기화
  $("#allResultsBody").empty();

  // sessionStorage에서 문제 정보 조회
  let questionsData = sessionStorage.getItem("questionsData");
  let questions = questionsData ? JSON.parse(questionsData) : [];

  // answerId별로 평가 결과 조회 (Backend 호출 필요)
  let allResultsData = JSON.parse(
    sessionStorage.getItem("allResultsData") || "[]"
  );

  if (allResultsData.length === 0) {
    $("#alertText").text("평가 결과 데이터가 없습니다.");
    openModal("alert-popup");
    return;
  }

  // 결과 데이터를 번호 순서대로 정렬
  allResultsData.sort((a, b) => a.order - b.order);

  allResultsData.forEach(function (result, idx) {
    let row = `
			<tr style="border-bottom: 1px solid #e0e0e0;">
				<td style="padding: 12px; text-align: left; border-right: 1px solid #ddd; font-size: 17px;">${
          result.order
        }</td>
				<td style="padding: 12px; text-align: left; border-right: 1px solid #ddd; word-break: break-word; max-width: 250px; font-size: 17px;">${replaceSpace(
          result.sentence
        )}</td>
				<td style="padding: 12px; text-align: center; border-right: 1px solid #ddd; font-weight: bold; font-size: 17px;">${
          result.sentenceScore
        }</td>
				<td style="padding: 12px; text-align: center; font-size: 17px;">${
          result.wordScores
        }</td>
			</tr>
		`;
    $("#allResultsBody").append(row);
  });

  openModal("all-results-popup");
}

// Excel 내보내기
function fn_exportExcel() {
  let userName = sessionStorage.getItem("userName");
  let answerIds = JSON.parse(sessionStorage.getItem("answerIds") || "[]");

  if (!userName) {
    $("#alertText").text("사용자 이름이 설정되지 않았습니다.");
    openModal("alert-popup");
    return;
  }

  if (answerIds.length === 0) {
    $("#alertText").text(
      "내보낼 평가 결과가 없습니다. 먼저 문제를 풀어주세요."
    );
    openModal("alert-popup");
    return;
  }

  let data = {
    userName: userName,
    answerIds: answerIds,
  };

  // Excel 다운로드 요청
  $.ajax({
    url: "/api/sp/demo/export/excel",
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(data),
    xhrFields: {
      responseType: "blob",
    },
    success: function (blob, status, xhr) {
      // 파일명 생성 (발음평가결과_이름_타임스탬프.xlsx)
      let timestamp = new Date()
        .toISOString()
        .replace(/[:.]/g, "-")
        .substring(0, 19);
      let filename = "발음평가결과_" + userName + "_" + timestamp + ".xlsx";

      // Blob을 파일로 다운로드
      let link = document.createElement("a");
      let url = window.URL.createObjectURL(blob);
      link.href = url;
      link.download = filename;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);

      $("#alertText").text("Excel 파일이 다운로드되었습니다.");
      openModal("alert-popup");
    },
    error: function (xhr, status, error) {
      $("#alertText").text("Excel 내보내기 중 오류가 발생했습니다.");
      openModal("alert-popup");
    },
  });
}
