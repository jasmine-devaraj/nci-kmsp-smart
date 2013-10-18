/** Returns true if the client browser is a member of the IE family */
function isIE() {
    return document.all && !window.opera;
}

/**  Returns true if the client browser is IE6  */
function isIE6() {
    return isIE() && (navigator.userAgent.toLowerCase().indexOf("msie 6.") != -1);
}

/**
 * Opens a classic new window.
 *
 * @param linkURL
 * @param w        new window width
 * @param h        new window height
 * @param x        new window x-coordinate
 * @param y        new window y-coordinate
 */
function openPopup(linkURL, w, h, x, y) {
  var params;
  params = "width = " + w;
  params += ", height = " + h;
  params += ", resizable = yes";
  params += ", scrollbars = yes";
  params += ", toolbar = no";
  params += ", menubar = no";
  params += ", status = no";
  params += ", directories = no";

  var newWindow = window.open(linkURL, "newWindow", params)
    newWindow.moveTo(x, y);
    newWindow.focus();
  }

/*
 * Finds the current "left" (x) position of the parent object from which the script
 * was fired. This is used to position popups near to their parent link rather
 * than in an asbolute or relative position within a document or page.
 */
function findPosX(obj) {
    var curleft = 0;
    if(obj.offsetParent) {
        while(1) {
          curleft += obj.offsetLeft;
          if(!obj.offsetParent) break;
          obj = obj.offsetParent;
        }
    } else if (obj.x) {
        curleft += obj.x;
    }
    return curleft;
  }

/*
 * Finds the current "top" (y) position of the parent object from which the script
 * was fired. This is used to position popups near to their parent link rather
 * than in an asbolute or relative position within a document or page.
 */
  function findPosY(obj) {
    var curtop = 0;
    if(obj.offsetParent) {
        while(1) {
          curtop += obj.offsetTop;
          if(!obj.offsetParent) break;
          obj = obj.offsetParent;
        }
    } else if(obj.y) {
        curtop += obj.y;
    }
    return curtop;
  }


function closePopup(obj) {
 document.getElementById(obj).style.display =  "none";
}


/**
 * Displays a DHTML modal popup on the web page. 
 *
 * @param obj the id of the hidden div to display
 */
function showModalPopup(obj) {
      if(obj == null || document.getElementById(obj) == null) return;
      // setup the veil over the page
      var ie=document.all && !window.opera;
      var standardbody=(document.compatMode=="CSS1Compat")? document.documentElement : document.body; //create reference to common "body" across doctypes
      var scroll_top=(ie)? standardbody.scrollTop : window.pageYOffset;
      var docwidth=(ie)? standardbody.clientWidth : window.innerWidth;         // The innerWidth property in FF does not include the scrollbar width
      var docheight=(ie)? standardbody.clientHeight: window.innerHeight;
      var docheightcomplete=(standardbody.offsetHeight>standardbody.scrollHeight)? standardbody.offsetHeight : standardbody.scrollHeight;
      var veilDivEl = document.getElementById('veilDiv');
      veilDivEl.style.width=docwidth+"px"; //set up veil over page
      veilDivEl.style.height=docheightcomplete+"px"; //set up veil over page
      veilDivEl.style.visibility="visible"; //Show veil over page

      var popup = document.getElementById(obj);
      var objwidth = popup.offsetWidth;
      var objheight = popup.offsetHeight;
      popup.style.visibility="visible"; //Show interstitial box
      popup.style.left=docwidth/2-objwidth/2+"px"; // Center popup horizontally
      var topposition=(docheight>objheight)? scroll_top+docheight/2-objheight/2+"px" : scroll_top+5+"px"; // Center popup vertically
      popup.style.top=Math.floor(parseInt(topposition))+"px";
    
      if (isIE6) {
         var html =
            "<iframe style=\"position: absolute; display: block; " +
            "z-index: -1; width: "+docwidth+"px; height: "+docheightcomplete+"px;top: -"+docheightcomplete/3+"px; left: -"+docwidth/3+"px;" +
            "filter: mask();\"></iframe>";
         if (popup) popup.innerHTML += html;
      }
}
/**
 * Displays a popup on the web page. Popups created by this script will automatically position their top-right
 * corner at the locaton from which they were called. To modify the positioning, provide an offset for x and
 * y. Note: if using the CSS attribute "filter: mask()" causes problems, a similar approach would be to use
 * the following in its place "filter: alpha(opacity=0); -moz-opacity: 0;".
 *
 * @param objLink  the id of the link from which this popup was called (used for positioning).
 * @param obj      the id of the hidden div to display
 * @param offsetX  the x offset for positioning
 * @param offsetY  the y offset for positioning
 */
function showPopup(objLink, obj, offsetX, offsetY) {

  var is_ie6 = document.all && (navigator.userAgent.toLowerCase().indexOf("msie 6.") != -1);
  var popup = document.getElementById(obj);
  var pwidth = parseInt(popup.style.width.replace("px","")) + 15;
  var pheight = parseInt(popup.style.height.replace("px","")) + 15;
  var link = document.getElementById(objLink);
  var posX = findPosX(link);
  var posY = findPosY(link);

      popup.style.left = posX + offsetX + "px";
      popup.style.top = posY + offsetY + "px";

//    alert("pwidth: " + pwidth + "\npheight: " + pheight);
  if (is_ie6) {
     var html =
        "<iframe style=\"position: absolute; display: block; " +
        "z-index: -1; width: " +pwidth+"px; height: "+pheight+ "px; top: -1px; left: -2px; overflow: hidden;" +
        "filter: mask(); border: 1px solid #CCCCCC; background-color: #FFFFFF; \"></iframe>";
     if (popup) popup.innerHTML += html;
  }
    popup.style.display = "block";
}

/******************************************************************************
/* Toggles the display of the search criteria table
/******************************************************************************/

var viewText = "View Search Criteria";
var hideText = "Hide Search Criteria";
var iconExpanded = "../images/icon-expanded-sign.gif";
var iconCollapsed = "../images/icon-collapsed-sign.gif";

function expandDiv(obj) {
    var o = document.getElementById(obj);
    var r = document.getElementById("searchCriteraLink");
    var i = document.getElementById("iconExpandCollapse");

    if ((o.style.display) == "block") {
        o.style.display = "none";
        i.setAttribute('src', iconCollapsed);
    } else {
        o.style.display = "block";
        i.setAttribute('src', iconExpanded);
    }

    if (r.innerHTML == viewText) {
        r.innerHTML = hideText;
    } else {
        r.innerHTML = viewText;
    }
}

function collapseDiv(obj) {
    document.getElementById(obj).style.display = "none";
    document.getElementById("searchCriteraLink").innerHTML = viewText;
}


