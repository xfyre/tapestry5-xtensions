tapestry5-xtensions
===================

This is a collection of Tapestry 5 components and mixins written to enhance 
overall UX and, in particular, Twitter Bootstrap integration. 

Initially it was created as a part of danceconvention.net, and moved to a separate
open-source project as a contribution to Apache Tapestry community.

## Usage

* Add ... Maven dependency to your project
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

Integration with Bootstrap modal component

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

* Twitter Typeahead: https://github.com/twitter/typeahead.js
* jQuery Hashchange plugin: http://benalman.com/projects/jquery-hashchange-plugin/

## Author

Ilya Obshadko


