tapestry5-xtensions
===================

This is a collection of Tapestry 5 components and mixins written to enhance 
overall UX and, in particular, Twitter Bootstrap integration. 

Initially it was created as a part of [danceconvention.net](https://danceconvention.net/), and moved to a separate
open-source project as a contribution to Apache Tapestry community.

## Installation

* Clone the project and run `mvn clean install` inside project's directory
* Add `xmlns:t5x="tapestry-library:t5xtensions"` to yor .tml root element

## Components

### AjaxUpload

AJAX upload component provides basic integration with Blueimp jQuery-File-Upload. Please note that you need to persist `uploadedFile` parameter elsewhere so it's not lost during form submission.

Basic usage:

```xml
<t5x:ajaxupload t:id="ajaxFileUpload" p:value="uploadedFile"/>
```

### DropdownField

Sets hidden field value from Bootstrap dropdown component. Acts very similar to standard `select` component (i.e. accepts `model` parameter or creates model automatically if `value` contains `enum` type.

```xml
<t5x:dropdownfield p:value="currency"/>
```

### FormatDate

Convenience component for formatting dates.

```xml
<t5x:formatdate p:date="dateValue" p:format="dd.MM.yyyy"/>
```

### FormErrors

Similar to native `<t:errors/>` component, but provides different layout (each error message has its own alert box).

```xml
<t5x:formerrors/>
```

### ModalDialog

Integration with Bootstrap modal component. Basically, to display modal, you need to create an empty zone and update this zone with injected block body during AJAX request.

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
    <t5x:modaldialog p:title="message:modal.title">
      <p:content>
        <p>Modal dialog body</p>
      </p:content>
    </t5x:modaldialog>
  </t:block>    
```

### SegmentedControl

Allows to present radio buttons as segmented buttons 

## Mixins

### AddAttribute

### BsConfirm

### BsPopover

### BsTooltip

### OnEnter

### OnEvent

### ToggleDropdown

### Typeahead

## Credits

* [Twitter Typeahead](https://github.com/twitter/typeahead.js)
* [jQuery Hashchange plugin](http://benalman.com/projects/jquery-hashchange-plugin/)

## Author

Ilya Obshadko


