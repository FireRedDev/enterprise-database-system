import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISystemuser } from '../systemuser.model';
import { SystemuserService } from '../service/systemuser.service';

@Component({
  templateUrl: './systemuser-delete-dialog.component.html',
})
export class SystemuserDeleteDialogComponent {
  systemuser?: ISystemuser;

  constructor(protected systemuserService: SystemuserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.systemuserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
