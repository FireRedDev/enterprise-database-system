import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TargetSystemCredentialsComponent } from './list/target-system-credentials.component';
import { TargetSystemCredentialsDetailComponent } from './detail/target-system-credentials-detail.component';
import { TargetSystemCredentialsUpdateComponent } from './update/target-system-credentials-update.component';
import { TargetSystemCredentialsDeleteDialogComponent } from './delete/target-system-credentials-delete-dialog.component';
import { TargetSystemCredentialsRoutingModule } from './route/target-system-credentials-routing.module';

@NgModule({
  imports: [SharedModule, TargetSystemCredentialsRoutingModule],
  declarations: [
    TargetSystemCredentialsComponent,
    TargetSystemCredentialsDetailComponent,
    TargetSystemCredentialsUpdateComponent,
    TargetSystemCredentialsDeleteDialogComponent,
  ],
  entryComponents: [TargetSystemCredentialsDeleteDialogComponent],
})
export class TargetSystemCredentialsModule {}
