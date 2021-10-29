import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITargetSystemCredentials } from '../target-system-credentials.model';
import { TargetSystemCredentialsService } from '../service/target-system-credentials.service';

@Component({
  templateUrl: './target-system-credentials-delete-dialog.component.html',
})
export class TargetSystemCredentialsDeleteDialogComponent {
  targetSystemCredentials?: ITargetSystemCredentials;

  constructor(protected targetSystemCredentialsService: TargetSystemCredentialsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.targetSystemCredentialsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
