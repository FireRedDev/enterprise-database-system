import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TargetsystemComponent } from './list/targetsystem.component';
import { TargetsystemDetailComponent } from './detail/targetsystem-detail.component';
import { TargetsystemUpdateComponent } from './update/targetsystem-update.component';
import { TargetsystemDeleteDialogComponent } from './delete/targetsystem-delete-dialog.component';
import { TargetsystemRoutingModule } from './route/targetsystem-routing.module';

@NgModule({
  imports: [SharedModule, TargetsystemRoutingModule],
  declarations: [TargetsystemComponent, TargetsystemDetailComponent, TargetsystemUpdateComponent, TargetsystemDeleteDialogComponent],
  entryComponents: [TargetsystemDeleteDialogComponent],
})
export class TargetsystemModule {}
