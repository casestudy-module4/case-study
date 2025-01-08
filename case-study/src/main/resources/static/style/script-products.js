(function (html) {
    html.className = html.className.replace(/\bno-js\b/, 'js')
})(document.documentElement);


/* <![CDATA[ */
window._wpemojiSettings = {
    "baseUrl": "https:\/\/s.w.org\/images\/core\/emoji\/14.0.0\/72x72\/",
    "ext": ".png",
    "svgUrl": "https:\/\/s.w.org\/images\/core\/emoji\/14.0.0\/svg\/",
    "svgExt": ".svg",
    "source": {"concatemoji": "https:\/\/tetviet.monamedia.net\/wp-includes\/js\/wp-emoji-release.min.js?ver=6.4.2"}
};
/*! This file is auto-generated */
!function (i, n) {
    var o, s, e;

    function c(e) {
        try {
            var t = {supportTests: e, timestamp: (new Date).valueOf()};
            sessionStorage.setItem(o, JSON.stringify(t))
        } catch (e) {
        }
    }

    function p(e, t, n) {
        e.clearRect(0, 0, e.canvas.width, e.canvas.height), e.fillText(t, 0, 0);
        var t = new Uint32Array(e.getImageData(0, 0, e.canvas.width, e.canvas.height).data),
            r = (e.clearRect(0, 0, e.canvas.width, e.canvas.height), e.fillText(n, 0, 0), new Uint32Array(e.getImageData(0, 0, e.canvas.width, e.canvas.height).data));
        return t.every(function (e, t) {
            return e === r[t]
        })
    }

    function u(e, t, n) {
        switch (t) {
            case"flag":
                return n(e, "\ud83c\udff3\ufe0f\u200d\u26a7\ufe0f", "\ud83c\udff3\ufe0f\u200b\u26a7\ufe0f") ? !1 : !n(e, "\ud83c\uddfa\ud83c\uddf3", "\ud83c\uddfa\u200b\ud83c\uddf3") && !n(e, "\ud83c\udff4\udb40\udc67\udb40\udc62\udb40\udc65\udb40\udc6e\udb40\udc67\udb40\udc7f", "\ud83c\udff4\u200b\udb40\udc67\u200b\udb40\udc62\u200b\udb40\udc65\u200b\udb40\udc6e\u200b\udb40\udc67\u200b\udb40\udc7f");
            case"emoji":
                return !n(e, "\ud83e\udef1\ud83c\udffb\u200d\ud83e\udef2\ud83c\udfff", "\ud83e\udef1\ud83c\udffb\u200b\ud83e\udef2\ud83c\udfff")
        }
        return !1
    }

    function f(e, t, n) {
        var r = "undefined" != typeof WorkerGlobalScope && self instanceof WorkerGlobalScope ? new OffscreenCanvas(300, 150) : i.createElement("canvas"),
            a = r.getContext("2d", {willReadFrequently: !0}),
            o = (a.textBaseline = "top", a.font = "600 32px Arial", {});
        return e.forEach(function (e) {
            o[e] = t(a, e, n)
        }), o
    }

    function t(e) {
        var t = i.createElement("script");
        t.src = e, t.defer = !0, i.head.appendChild(t)
    }

    "undefined" != typeof Promise && (o = "wpEmojiSettingsSupports", s = ["flag", "emoji"], n.supports = {
        everything: !0,
        everythingExceptFlag: !0
    }, e = new Promise(function (e) {
        i.addEventListener("DOMContentLoaded", e, {once: !0})
    }), new Promise(function (t) {
        var n = function () {
            try {
                var e = JSON.parse(sessionStorage.getItem(o));
                if ("object" == typeof e && "number" == typeof e.timestamp && (new Date).valueOf() < e.timestamp + 604800 && "object" == typeof e.supportTests) return e.supportTests
            } catch (e) {
            }
            return null
        }();
        if (!n) {
            if ("undefined" != typeof Worker && "undefined" != typeof OffscreenCanvas && "undefined" != typeof URL && URL.createObjectURL && "undefined" != typeof Blob) try {
                var e = "postMessage(" + f.toString() + "(" + [JSON.stringify(s), u.toString(), p.toString()].join(",") + "));",
                    r = new Blob([e], {type: "text/javascript"}),
                    a = new Worker(URL.createObjectURL(r), {name: "wpTestEmojiSupports"});
                return void (a.onmessage = function (e) {
                    c(n = e.data), a.terminate(), t(n)
                })
            } catch (e) {
            }
            c(n = f(s, u, p))
        }
        t(n)
    }).then(function (e) {
        for (var t in e) n.supports[t] = e[t], n.supports.everything = n.supports.everything && n.supports[t], "flag" !== t && (n.supports.everythingExceptFlag = n.supports.everythingExceptFlag && n.supports[t]);
        n.supports.everythingExceptFlag = n.supports.everythingExceptFlag && !n.supports.flag, n.DOMReady = !1, n.readyCallback = function () {
            n.DOMReady = !0
        }
    }).then(function () {
        return e
    }).then(function () {
        var e;
        n.supports.everything || (n.readyCallback(), (e = n.source || {}).concatemoji ? t(e.concatemoji) : e.wpemoji && e.twemoji && (t(e.twemoji), t(e.wpemoji)))
    }))
}((window, document), window._wpemojiSettings);
/* ]]> */


window._nslDOMReady = function (callback) {
    if (document.readyState === "complete" || document.readyState === "interactive") {
        callback();
    } else {
        document.addEventListener("DOMContentLoaded", callback);
    }
};


/* <![CDATA[ */
var wc_add_to_cart_params = {
    "ajax_url": "\/wp-admin\/admin-ajax.php",
    "wc_ajax_url": "\/?wc-ajax=%%endpoint%%",
    "i18n_view_cart": "Xem gi\u1ecf h\u00e0ng",
    "cart_url": "https:\/\/tetviet.monamedia.net\/gio-hang\/",
    "is_cart": "",
    "cart_redirect_after_add": "no"
};
/* ]]> */


/* <![CDATA[ */
var woocommerce_params = {"ajax_url": "\/wp-admin\/admin-ajax.php", "wc_ajax_url": "\/?wc-ajax=%%endpoint%%"};
/* ]]> */


var essb_settings = {
    "ajax_url": "https:\/\/tetviet.monamedia.net\/wp-admin\/admin-ajax.php",
    "essb3_nonce": "f8d164a64f",
    "essb3_plugin_url": "https:\/\/tetviet.monamedia.net\/wp-content\/plugins\/easy-social-share-buttons3",
    "essb3_facebook_total": true,
    "essb3_admin_ajax": false,
    "essb3_internal_counter": false,
    "essb3_stats": false,
    "essb3_ga": false,
    "essb3_ga_mode": "simple",
    "essb3_counter_button_min": 0,
    "essb3_counter_total_min": 0,
    "blog_url": "https:\/\/tetviet.monamedia.net\/",
    "ajax_type": "wp",
    "essb3_postfloat_stay": false,
    "essb3_no_counter_mailprint": false,
    "essb3_single_ajax": false,
    "twitter_counter": "self",
    "post_id": 621
};


(function () {
    var c = document.body.className;
    c = c.replace(/woocommerce-no-js/, 'woocommerce-js');
    document.body.className = c;
})();


/* <![CDATA[ */
var wpcf7 = {"api": {"root": "https:\/\/tetviet.monamedia.net\/wp-json\/", "namespace": "contact-form-7\/v1"}};
/* ]]> */


/* <![CDATA[ */
var flatsomeVars = {
    "theme": {"version": "3.16.2"},
    "ajaxurl": "https:\/\/tetviet.monamedia.net\/wp-admin\/admin-ajax.php",
    "rtl": "",
    "sticky_height": "90",
    "assets_url": "https:\/\/tetviet.monamedia.net\/wp-content\/themes\/flatsome\/assets\/js\/",
    "lightbox": {
        "close_markup": "<button title=\"%title%\" type=\"button\" class=\"mfp-close\"><svg xmlns=\"http:\/\/www.w3.org\/2000\/svg\" width=\"28\" height=\"28\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\" class=\"feather feather-x\"><line x1=\"18\" y1=\"6\" x2=\"6\" y2=\"18\"><\/line><line x1=\"6\" y1=\"6\" x2=\"18\" y2=\"18\"><\/line><\/svg><\/button>",
        "close_btn_inside": false
    },
    "user": {"can_edit_pages": false},
    "i18n": {"mainMenu": "Main Menu", "toggleButton": "Toggle"},
    "options": {
        "cookie_notice_version": "1",
        "swatches_layout": false,
        "swatches_box_select_event": false,
        "swatches_box_behavior_selected": false,
        "swatches_box_update_urls": "1",
        "swatches_box_reset": false,
        "swatches_box_reset_extent": false,
        "swatches_box_reset_time": 300,
        "search_result_latency": "0"
    },
    "is_mini_cart_reveal": "1"
};
/* ]]> */


/* <![CDATA[ */
var _zxcvbnSettings = {"src": "https:\/\/tetviet.monamedia.net\/wp-includes\/js\/zxcvbn.min.js"};
/* ]]> */


/* <![CDATA[ */
wp.i18n.setLocaleData({'text direction\u0004ltr': ['ltr']});
/* ]]> */


/* <![CDATA[ */
var pwsL10n = {
    "unknown": "M\u1eadt kh\u1ea9u m\u1ea1nh kh\u00f4ng x\u00e1c \u0111\u1ecbnh",
    "short": "R\u1ea5t y\u1ebfu",
    "bad": "Y\u1ebfu",
    "good": "Trung b\u00ecnh",
    "strong": "M\u1ea1nh",
    "mismatch": "M\u1eadt kh\u1ea9u kh\u00f4ng kh\u1edbp"
};
/* ]]> */


/* <![CDATA[ */
(function (domain, translations) {
    var localeData = translations.locale_data[domain] || translations.locale_data.messages;
    localeData[""].domain = domain;
    wp.i18n.setLocaleData(localeData, domain);
})("default", {
    "translation-revision-date": "2023-07-15 15:29:09+0000",
    "generator": "GlotPress\/4.0.0-alpha.9",
    "domain": "messages",
    "locale_data": {
        "messages": {
            "": {
                "domain": "messages",
                "plural-forms": "nplurals=1; plural=0;",
                "lang": "vi_VN"
            },
            "%1$s is deprecated since version %2$s! Use %3$s instead. Please consider writing more inclusive code.": ["%1$s \u0111\u00e3 ng\u1eebng ho\u1ea1t \u0111\u1ed9ng t\u1eeb phi\u00ean b\u1ea3n %2$s! S\u1eed d\u1ee5ng thay th\u1ebf b\u1eb1ng %3$s."]
        }
    },
    "comment": {"reference": "wp-admin\/js\/password-strength-meter.js"}
});
/* ]]> */


/* <![CDATA[ */
var wc_password_strength_meter_params = {
    "min_password_strength": "3",
    "stop_checkout": "",
    "i18n_password_error": "Vui l\u00f2ng nh\u1eadp m\u1eadt kh\u1ea9u kh\u00f3 h\u01a1n.",
    "i18n_password_hint": "G\u1ee3i \u00fd: M\u1eadt kh\u1ea9u ph\u1ea3i c\u00f3 \u00edt nh\u1ea5t 12 k\u00fd t\u1ef1. \u0110\u1ec3 n\u00e2ng cao \u0111\u1ed9 b\u1ea3o m\u1eadt, s\u1eed d\u1ee5ng ch\u1eef in hoa, in th\u01b0\u1eddng, ch\u1eef s\u1ed1 v\u00e0 c\u00e1c k\u00fd t\u1ef1 \u0111\u1eb7c bi\u1ec7t nh\u01b0 ! \" ? $ % ^ & )."
};
/* ]]> */


<!-- WooCommerce JavaScript -->
jQuery(function ($) {

    jQuery('.dropdown_product_cat').on('change', function () {
        if (jQuery(this).val() != '') {
            var this_page = '';
            var home_url = 'https://tetviet.monamedia.net/';
            if (home_url.indexOf('?') > 0) {
                this_page = home_url + '&product_cat=' + jQuery(this).val();
            } else {
                this_page = home_url + '?product_cat=' + jQuery(this).val();
            }
            location.href = this_page;
        } else {
            location.href = 'https://tetviet.monamedia.net/cua-hang/';
        }
    });

    if (jQuery().selectWoo) {
        var wc_product_cat_select = function () {
            jQuery('.dropdown_product_cat').selectWoo({
                placeholder: 'Chọn danh mục',
                minimumResultsForSearch: 5,
                width: '100%',
                allowClear: true,
                language: {
                    noResults: function () {
                        return 'Không tìm thấy kết quả phù hợp';
                    }
                }
            });
        };
        wc_product_cat_select();
    }

});


var essb_clicked_lovethis = false;
var essb_love_you_message_thanks = "Thank you for loving this.";
var essb_love_you_message_loved = "You already love this today.";
var essb_lovethis = function (oInstance) {
    if (essb_clicked_lovethis) {
        alert(essb_love_you_message_loved);
        return;
    }
    var element = jQuery('.essb_' + oInstance);
    if (!element.length) {
        return;
    }
    var instance_post_id = jQuery(element).attr("data-essb-postid") || "";
    var cookie_set = essb_get_lovecookie("essb_love_" + instance_post_id);
    if (cookie_set) {
        alert(essb_love_you_message_loved);
        return;
    }
    if (typeof (essb_settings) != "undefined") {
        jQuery.post(essb_settings.ajax_url, {
            'action': 'essb_love_action',
            'post_id': instance_post_id,
            'service': 'love',
            'nonce': essb_settings.essb3_nonce
        }, function (data) {
            if (data) {
                alert(essb_love_you_message_thanks);
            }
        }, 'json');
    }
    essb_tracking_only('', 'love', oInstance, true);
};
var essb_get_lovecookie = function (name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
};
var essb_window = function (oUrl, oService, oInstance) {
    var element = jQuery('.essb_' + oInstance);
    var instance_post_id = jQuery(element).attr("data-essb-postid") || "";
    var instance_position = jQuery(element).attr("data-essb-position") || "";
    var wnd;
    var w = 800;
    var h = 500;
    if (oService == "twitter") {
        w = 500;
        h = 300;
    }
    var left = (screen.width / 2) - (w / 2);
    var top = (screen.height / 2) - (h / 2);
    if (oService == "twitter") {
        wnd = window.open(oUrl, "essb_share_window", "height=300,width=500,resizable=1,scrollbars=yes,top=" + top + ",left=" + left);
    } else {
        wnd = window.open(oUrl, "essb_share_window", "height=500,width=800,resizable=1,scrollbars=yes,top=" + top + ",left=" + left);
    }
    if (typeof (essb_settings) != "undefined") {
        if (essb_settings.essb3_stats) {
            if (typeof (essb_handle_stats) != "undefined") {
                essb_handle_stats(oService, instance_post_id, oInstance);
            }
        }
        if (essb_settings.essb3_ga) {
            essb_ga_tracking(oService, oUrl, instance_position);
        }
    }
    essb_self_postcount(oService, instance_post_id);
    var pollTimer = window.setInterval(function () {
        if (wnd.closed !== false) {
            window.clearInterval(pollTimer);
            essb_smart_onclose_events(oService, instance_post_id);
        }
    }, 200);
};
var essb_self_postcount = function (oService, oCountID) {
    if (typeof (essb_settings) != "undefined") {
        oCountID = String(oCountID);
        jQuery.post(essb_settings.ajax_url, {
            'action': 'essb_self_postcount',
            'post_id': oCountID,
            'service': oService,
            'nonce': essb_settings.essb3_nonce
        }, function (data) {
            if (data) {
            }
        }, 'json');
    }
};
var essb_smart_onclose_events = function (oService, oPostID) {
    if (typeof (essbasc_popup_show) == 'function') {
        essbasc_popup_show();
    }
    if (typeof essb_acs_code == 'function') {
        essb_acs_code(oService, oPostID);
    }
};
var essb_tracking_only = function (oUrl, oService, oInstance, oAfterShare) {
    var element = jQuery('.essb_' + oInstance);
    if (oUrl == "") {
        oUrl = document.URL;
    }
    var instance_post_id = jQuery(element).attr("data-essb-postid") || "";
    var instance_position = jQuery(element).attr("data-essb-position") || "";
    if (typeof (essb_settings) != "undefined") {
        if (essb_settings.essb3_stats) {
            if (typeof (essb_handle_stats) != "undefined") {
                essb_handle_stats(oService, instance_post_id, oInstance);
            }
        }
        if (essb_settings.essb3_ga) {
            essb_ga_tracking(oService, oUrl, instance_position);
        }
    }
    essb_self_postcount(oService, instance_post_id);
    if (oAfterShare) {
        essb_smart_onclose_events(oService, instance_post_id);
    }
};
var essb_pinterest_picker = function (oInstance) {
    essb_tracking_only('', 'pinterest', oInstance);
    var e = document.createElement('script');
    e.setAttribute('type', 'text/javascript');
    e.setAttribute('charset', 'UTF-8');
    e.setAttribute('src', '//assets.pinterest.com/js/pinmarklet.js?r=' + Math.random() * 99999999);
    document.body.appendChild(e);
};
