/**
 * Loops through input checkbox elements in the form and applies a CSS class to the checked
 * elements' parent node (typically a list item).
 *
 * @param formId the form's ID
 * @param c the class to apply
 */
function highlightSelectedInForm(formId, c) {
    var form = document.getElementById(formId);
    for (var i = 0; i < form.elements.length; i++) {
        var e = form.elements[i];
        if (e.type == "checkbox" && e.checked)
            e.parentNode.className = c;
    }
}

/**
 * Attaches a listener to each checkbox element in the form. Due to cross-browser compatibility
 * problems (I am looking at you IE6), we cannot yet use the DOM addEventListener() without
 * hassle. For this reason, this function uses the traditional method. 
 *
 * @param formId the form's ID
 * @param highlightClass the CSS class for highlighting
 */
function attachCheckboxListener(formId, highlightClass) {
    var form = document.getElementById(formId);
    var checkboxes = form.getElementsByTagName("input");
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].type == "checkbox") {
            checkboxes[i].onclick = function () { handleCheckboxClick(this, highlightClass); };
        }
    }
}
/**
 * Performs the necessary checkbox handling for NFRP search forms. This function performs two
 * primary tasks. First, it ensures that when "Any [item]" is checked, all other elements in that
 * list of checkboxes is unchecked, and vice versa. Secondly, it perfoms simple highlighting.
 *
 * @param e the input checkbox element
 * @param highlightClass the CSS class for highlighting
 */
function handleCheckboxClick(e, highlightClass) {

    // if e is an input checkbox, then parentNode is a list element, its and parentNode is an
    // unordered list. Getting all of the tags by "input" will therefore return all of the related
    // inputs in this respective list
    var inputs = e.parentNode.parentNode.getElementsByTagName("input");

    // The following block handles the checkbox lists that contain "Any [items]." If the user
    // selects anything other than "Any [item]", then "Any..." must be deselected. Otherwise,
    // if the user selects "Any..." then all other items currently checked must be deselected. In
    // each group of checkboxes, the "Any..." option is represented with an empty string value ("").

    if (e.value == "") {                                       // If the user clicked the "Any..." checkbox
        for (var j = 0; j < inputs.length; j++) {              // loop through all of this list's checkboxes
            if (inputs[j].value != "" && inputs[j].checked) {  // if any item (other than "Any...") is checked
                inputs[j].checked = "";                        // uncheck it and
                inputs[j].parentNode.className = "";           // unhighlight it
            }
        }
    } else {                                                   // Otherwise, the user clicked another checkbox
        for (var i = 0; i < inputs.length; i++) {              // loop through this list's checkboxes
            if (inputs[i].value == "" && inputs[i].checked) {  // find the "Any..." checkbox
                inputs[i].checked = "";                        // uncheck it
                inputs[i].parentNode.className = "";           // unhighlight it
            }
        }
    }

    // Finally, perform simple highlighting
    if (!e.checked)
        e.parentNode.className = "";
     else
        e.parentNode.className = highlightClass;
}


function resetField(f) {
    document.getElementById(f).selectedIndex = 0;
}

function clearField(f) {
    document.getElementById(f).value = "";
}