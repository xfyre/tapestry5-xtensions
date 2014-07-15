tapestry5-xtensions
===================

This is a collection of Tapestry 5 components and mixins written to enhance 
overall UX and, in particular, Twitter Bootstrap integration. 

Initially it was created as a part of [danceconvention.net](https://danceconvention.net/), 
and moved to separate open-source project as a contribution to Apache Tapestry community.

## Installation

* Clone the project and run `mvn clean install` inside project's directory
* Add `xmlns:t5x="tapestry-library:t5xtensions"` to yor .tml root element

## Components

### AjaxUpload

AJAX upload component providing basic integration with Blueimp jQuery-File-Upload. Please note that you need to persist 
`uploadedFile` parameter elsewhere so it's not lost during form submission. Currently only single-file AJAX upload is 
supported - feel free to fork and modify as needed.

Basic usage:

```xml
<t5x:ajaxupload t:id="ajaxFileUpload" p:value="uploadedFile"/>
```

### DropdownField

Wrapper around Tapestry `Hidden` component that receives its value from Bootstrap drop-down. 
Acts very similar to standard `Select` component (i.e. accepts `model` parameter or creates model 
automatically if `value` contains `enum` type.

```xml
<t5x:dropdownfield p:value="currency"/>
```

### FormatDate

Convenience component for formatting dates.

```xml
<t5x:formatdate p:date="dateValue" p:format="dd.MM.yyyy"/>
```

### FormErrors

Similar to Tapestry `Errors` component, but provides different layout (each error message has its own alert box).

```xml
<t5x:formerrors/>
```

### ModalDialog

Integration with Bootstrap modal component. Basically, to display modal, you need to create an empty zone and update 
this zone with injected block body during AJAX request.

```java
    @Inject
    private Block modalDialogBlock;
    
    Object onActionFromShowModal () {
        // do something
        return modalDialogBlock;
    }
```

```xml
  <t:zone t:id="modalDialogZone" id="modalDialogZone">
  </t:zone>
  
  <t:actionlink t:id="showModal" p:zone="modalDialogZone">Show modal</t:actionlink>
  
  <t:block t:id="modalDialogBlock">
    <t5x:modaldialog p:title="literal:Modal dialog title">
      <p:content>
        <p>Modal dialog body</p>
      </p:content>
    </t5x:modaldialog>
  </t:block>    
```

### SegmentedControl

Segmented control backed by radio group. Looks similar to its iOS `UISegmentedControl` counterpart. Like `select` component, accepts `model` parameter.

```xml
<t5x:segmentedcontrol t:id="segmentedControl" p:model="valuesModel" p:value="value"/>
```

## Mixins

### AddAttribute

Allows to add attributes to elements (using JavaScript) after page rendering depending on selected conditions.
For example, make a field read-only if certain condition is met.

```xml
<t:textfield p:value="value" t:mixins="t5xtensions/addattribute" 
             p:condition="someCondition" p:attrname="readonly" p:attrvalue="readonly"/>
```

### BsConfirm

Confirmation dialog using Bootstrap. Similar to Tapestry built-in `Confirm` component, but more flexible.

```xml
<button t:id="submitDelete" t:type="submit" class="btn btn-danger" p:value="message:button.delete"
        t:mixins="t5xtensions/bsconfirm" p:message="prop:removalConfirmation" p:confirmClass="btn-danger"/>

```

### BsPopover

Bootstrap popover component integration. Uses Boostrap `data-*` attributes, doesn't require any additional parameters.

```xml
<a href="#" class="btn btn-sm btn-success" onclick="return false;"
  t:type="any" t:mixins="t5xtensions/bspopover" 
  data-toggle="popover" data-trigger="hover" data-html="true" 
  data-title="Some title" 
  data-content="Popover content" data-placement="right">
  <span class="glyphicon glyphicon-ok-sign glyphicon-white"/>
</a>
```

### BsTooltip

Bootstrap tooltip component integration. Uses Boostrap `data-*` attributes, doesn't require any additional parameters.

```xml
<span t:type="any" class="btn btn-warning btn-xs" t:mixins="t5xtensions/bstooltip" 
      title="Tooltip text" data-toggle="tooltip" data-placement="top">
  <i class="glyphicon glyphicon-exclamation-sign glyphicon-white"/>
</span>
```

### OnEnter

This component lets you trigger form submit from the specified submit element to handle sitiations 
when user presses enter within a text form control by mistake. Triggering form submit from a specified 
element allows you to bypass normal form submission in your server-side code using `onSelected` submission
handlers.

```xml
<t:form p:zone="zone" t:mixins="t5xtensions/onenter" p:submitElement="prop:defaultSubmitId">
<t:submit t:id="submitDefault" class="hidden"/>
<!-- some other form controls -->
</t:form>
```

```java
    void onSelectedFromSubmitDefault () {
        this.skipValidation = true;
    }
```

### OnEvent

Allows to update a zone or trigger form submit on specified client event.

For example, submit the form when `change` event is triggered on `select`:

```xml
<t:select p:value="selectedValue" p:model="selectModel"
          t:mixins="t5xtensions/onevent" p:submitElement="prop:hiddenSubmitId" p:clientEvent="change"/>
```

Update specified zone when radio button is selected:

```xml
<t:radio p:value="someValue" t:mixins="t5xtensions/onevent" 
         p:serverevent="reloadzone" p:updatezone="prop:zoneId"/>
```

Check component source code for more options.

### ToggleDropdown

Convenience mixin that toggles off Bootstrap dropdown when dropdown action is an AJAX request (otherwise dropdown doesn't toggle).

```xml
<button class="btn btn-default dropdown-toggle" data-toggle="dropdown" id="showLinks">
  <span class="glyphicon glyphicon-plus"/>&#160;Show some links&#160;<span class="caret"/>
</button>
<ul class="dropdown-menu">
  <li><t:actionlink t:id="link1" p:zone="zone" t:mixins="t5xtensions/ToggleDropdown" p:dropdownid="showLinks">
      Ajax Action 1</t:actionlink>
  </li>
  <li><t:actionlink t:id="link2" p:zone="zone" t:mixins="t5xtensions/ToggleDropdown" p:dropdownid="showLinks">
      Ajax Action 2</t:actionlink>
  </li>
</ul>
```

### Typeahead

Replacement for built-in Tapestry Autocomplete mixin. Provides much more flexibility: allows you to specify suggestion templates, 
can handle list of `Map` objects or list of beans as completion results.

```xml
<t:textfield p:value="value" t:mixins="t5xtensions/typeahead" 
             p:keys="literal:username,email" p:template="literal:{{username}}, {{email}}"/>
```

```java
    List<MyUser> onCompletionsRequestedFromUserLookupField ( String partial ) {
        return userManagerService.getUserSuggestions ( partial );
    }
```

*Note*: when you are using list of beans or list of `Map` objects for suggestions, all the keys you have specified in `keys` parameter
must be present as bean properties or map keys, respectively. Otherwise you get an error.  

## Credits

* [Apache Tapestry](http://tapestry.apache.org/)
* [Twitter Typeahead](https://github.com/twitter/typeahead.js)
* [jQuery Hashchange plugin](http://benalman.com/projects/jquery-hashchange-plugin/)

## Author

Ilya Obshadko


