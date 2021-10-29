import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITargetSystem } from '../target-system.model';
import { TargetSystemService } from '../service/target-system.service';

@Component({
  templateUrl: './target-system-delete-dialog.component.html',
})
export class TargetSystemDeleteDialogComponent {
  targetSystem?: ITargetSystem;

  constructor(protected targetSystemService: TargetSystemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.targetSystemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
