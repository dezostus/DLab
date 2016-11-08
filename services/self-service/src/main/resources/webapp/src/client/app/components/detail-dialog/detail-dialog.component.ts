/******************************************************************************************************

 Copyright (c) 2016 EPAM Systems Inc.

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 *****************************************************************************************************/

import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { Modal } from './../modal/modal.component';
import {DateUtils} from './../../util/dateUtils'


 @Component({
   moduleId: module.id,
   selector: 'detail-dialog',
   templateUrl: 'detail-dialog.component.html'
 })

 export class DetailDialog {
 	notebook: any;
 	upTimeInHours: number;


 	@ViewChild('bindDialog') bindDialog;

 	open(param, notebook) {
 	  if(notebook.time)
      this.upTimeInHours = DateUtils.diffBetweenDatesInHours(notebook.time);
    this.notebook = notebook;
    this.bindDialog.open(param);
   }
 }

