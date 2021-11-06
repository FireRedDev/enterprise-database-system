import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITargetsystem } from '../targetsystem.model';
import { TargetsystemService } from '../service/targetsystem.service';

@Component({
  templateUrl: './targetsystem-delete-dialog.component.html',
})
export class TargetsystemDeleteDialogComponent {
  targetsystem?: ITargetsystem;

  constructor(protected targetsystemService: TargetsystemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.targetsystemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
