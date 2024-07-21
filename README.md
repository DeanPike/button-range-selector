# Button range selector

This button range selector allows the selection of ranges and single values.

The first button selected will select an individual value.
The next selection will select a range.

After this the component will switch between individual and range selection.

For example
If we have provided the following range to the list 1,2,3,4,5

The first time the user selects the value 1. 
The returned minimum and maximum will be 0 as 1 is the first position in the list.

The next time the user selects the value 4, 
The returned minimum value will be 0 and the maximum will be 3. 