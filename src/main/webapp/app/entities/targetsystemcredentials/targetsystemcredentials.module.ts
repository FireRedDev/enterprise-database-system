import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TargetsystemcredentialsComponent } from './list/targetsystemcredentials.component';
import { TargetsystemcredentialsDetailComponent } from './detail/targetsystemcredentials-detail.component';
import { TargetsystemcredentialsUpdateComponent } from './update/targetsystemcredentials-update.component';
import { TargetsystemcredentialsDeleteDialogComponent } from './delete/targetsystemcredentials-delete-dialog.component';
import { TargetsystemcredentialsRoutingModule } from './route/targetsystemcredentials-routing.module';

@NgModule({
  imports: [SharedModule, TargetsystemcredentialsRoutingModule],
  declarations: [
    TargetsystemcredentialsComponent,
    TargetsystemcredentialsDetailComponent,
    TargetsystemcredentialsUpdateComponent,
    TargetsystemcredentialsDeleteDialogComponent,
  ],
  entryComponents: [TargetsystemcredentialsDeleteDialogComponent],
})
export class TargetsystemcredentialsModule {}
