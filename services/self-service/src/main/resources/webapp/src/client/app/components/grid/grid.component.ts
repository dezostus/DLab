/******************************************************************************************************

Copyright (c) 2016 EPAM Systems Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

*****************************************************************************************************/

import { Component, EventEmitter, Input, Output, ViewChild } from "@angular/core";
import { EnvironmentsService } from './../../services/environments.service';
import { GridRowModel } from './grid.model';

@Component({
  moduleId: module.id,
  selector: 'ng-grid',
  templateUrl: 'grid.component.html',
  styleUrls: ['./grid.component.css']
})

export class Grid {

  list: Array<any>;
  environments: Array<GridRowModel>;
  notebook: Object;


  @ViewChild('createEmrModal') createEmrModal;
  @Input() emrTempls;
  @Input() shapes;


  constructor(private environmentsService: EnvironmentsService) { }

  ngOnInit() {
    this.environmentsService.getEnvironmentsList().subscribe((list) => {
      this.list = list['RESOURCES'];

      this.environments = this.loadEnvironments();
      console.log('models ', this.environments);
    });
  }

  loadEnvironments(): Array<any> {
    return this.list.map((value) => {
      return new GridRowModel(value.ENVIRONMENT_NAME,
        value.STATUS,
        value.SHAPE,
        value.COMPUTATIONAL_RESOURCES);
    });
  }

  printDetailEnvironmentModal(data) {
    console.log(data);
  }

  mathAction(data, action) {
    console.log('action ' + action, data);
    if(action === 'deploy') {
      this.notebook = data;
      this.createEmrModal.open();
    }

  }
}
