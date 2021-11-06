import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITargetsystemcredentials } from '../targetsystemcredentials.model';
import { TargetsystemcredentialsService } from '../service/targetsystemcredentials.service';

@Component({
  templateUrl: './targetsystemcredentials-delete-dialog.component.html',
})
export class TargetsystemcredentialsDeleteDialogComponent {
  targetsystemcredentials?: ITargetsystemcredentials;

  constructor(protected targetsystemcredentialsService: TargetsystemcredentialsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.targetsystemcredentialsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
