$(function() {
  AOS.init({
    once: true,
    duration: 1000
  });

  headerScroll();
  accordionList();
  selectList();
  headerNav();
});

function headerScroll() {
  var didScroll;
  var lastScrollTop = 0;
  var delta = 5;
  var navbarHeight = $('header').outerHeight() - 100;

  $(window).scroll(function(event){
      didScroll = true;
  });

  setInterval(function() {
      if (didScroll) {
          hasScrolled();
          didScroll = false;
      }
  }, 250);

  function hasScrolled() {
      var st = $(this).scrollTop();

      if(Math.abs(lastScrollTop - st) <= delta)
          return;

      if (st > lastScrollTop && st > navbarHeight){
          $('header').removeClass('nav-down').addClass('nav-up');
      } else {
          if(st + $(window).height() < $(document).height()) {
              $('header').removeClass('nav-up').addClass('nav-down');
          }
      }

      if (st == 0) {
          $('header').removeClass('nav-down');
          $('header').removeClass('nav-up');
      }

      lastScrollTop = st;
  }

}


function headerNav() {
  $(document).on("mouseover","#header #nav .nav-list .list > a",function() {
    $(this).siblings('.dep02').stop(true, true).slideDown();
    $(this).addClass('active');
  });

  $(document).on("mouseleave","#header #nav .nav-list .list",function() {
    $(this).find('.dep02').stop(true, true).slideUp();
    $(this).find('a').removeClass('active');
  });
}


function accordionList() {
  const items = document.querySelectorAll(".accordion-box button");

  function toggleAccordion() {
    const itemToggle = this.getAttribute('aria-expanded');

    for (i = 0; i < items.length; i++) {
      items[i].setAttribute('aria-expanded', 'false');
    }

    if (itemToggle == 'false') {
      this.setAttribute('aria-expanded', 'true');
    }
  }

  items.forEach(item => item.addEventListener('click', toggleAccordion));
}


function selectList() {

  $(document).on("click", ".form-input", function (e) {
    e.stopPropagation();
    let $this = $(this);

    $(".form-input").not($this).removeClass("active");
    $this.addClass("active");
  });

  $(document).on("click", ".select-list li", function (e) {
    e.stopPropagation();
    let $formInput = $(this).closest(".form-input");

    let selectedText = $(this).text().trim();
    $formInput.find(".select-input p").text(selectedText);

    $formInput.removeClass("active");
  });

  $(document).on("click", function () {
    $(".form-input").removeClass("active");
  });

  $(document).on("keydown", ".select-list li", function (e) {
    if (e.key === "Enter" || e.key === " ") {
      e.preventDefault();

      $(this).trigger("click");
    }
  });

  $(document).on("keydown", function (e) {
    if (e.key === "Tab") {
      setTimeout(() => {
        if (!$(":focus").closest(".form-input").length) {
          $(".form-input").removeClass("active");
        }
      }, 10);
    }
  });

  $(document).on("focusin", ".select-input, .select-list li", function () {
    $(this).closest(".form-input").addClass("active");
  });

  $(document).on("focusout", ".select-input, .select-list li", function () {
    setTimeout(() => {
      if (!$(this).closest(".form-input").find(":focus").length) {
        $(this).closest(".form-input").removeClass("active");
      }
    }, 50);
  });


  // check box 구현
  $(document).on("keydown", "label", function (e) {
    if (e.key === "Enter" || e.key === " " || e.keyCode === 13 || e.keyCode === 32) {
        e.preventDefault();
        const input = $(this).prev("input[type=radio]");
        if (!input.prop("checked")) {
            input.prop("checked", true).trigger("change");
        }
    }
  });
}



//MODAL
function openModal(modalname, callback) {
  var modal = $("." + modalname);
  modal.fadeIn(300);

  var closeBtn = modal.find(".close-btn");
  if (closeBtn.length) {
    closeBtn.attr("tabindex", "0");
    closeBtn.focus();
  }

  $(document).on("click", ".popup-wrap .close-btn", function(e) {
    e.preventDefault();
    modal.fadeOut(300);
    if (callback && typeof callback === 'function') {
      callback();
    }
  });

  $(document).on("keydown", ".popup-wrap .close-btn", function(e) {
    if (e.key === "Enter" || e.key === " " || e.keyCode === 13 || e.keyCode === 32) {  // Enter or Space key
      e.preventDefault();
      $(this).click();
    }
  });

  $(".btn, .news-pagination .paging li a").on("click", function(e) {
    e.preventDefault();

    var href = $(this).attr("href");

    if (!href) return;

    var match = href.match(/'([^']+)'/);

    if (match && match[1]) {
      var modalClass = match[1];
      openModal(modalClass);
    }
  });

  $(document).on("keyup", function(e) {
    if (e.key === "Enter") {
      var focusedElement = document.activeElement;

      if ($(focusedElement).hasClass('btn') || $(focusedElement).closest('.news-pagination .paging li a').length) {
        var modalClass = $(focusedElement).attr("href").match(/'([^']+)'/)[1];  // href에서 modalClass 추출
        openModal(modalClass);
      }
    }
  });
}


$('.close-btn').on('click keydown', function(event) {
  if (event.type === 'click' || event.key === 'Enter' || event.key === ' ') {
    $(this).parents('.popup-wrap').hide();
    event.preventDefault();
  }
});