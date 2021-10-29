import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TargetSystemComponent } from './list/target-system.component';
import { TargetSystemDetailComponent } from './detail/target-system-detail.component';
import { TargetSystemUpdateComponent } from './update/target-system-update.component';
import { TargetSystemDeleteDialogComponent } from './delete/target-system-delete-dialog.component';
import { TargetSystemRoutingModule } from './route/target-system-routing.module';

@NgModule({
  imports: [SharedModule, TargetSystemRoutingModule],
  declarations: [TargetSystemComponent, TargetSystemDetailComponent, TargetSystemUpdateComponent, TargetSystemDeleteDialogComponent],
  entryComponents: [TargetSystemDeleteDialogComponent],
})
export class TargetSystemModule {}
