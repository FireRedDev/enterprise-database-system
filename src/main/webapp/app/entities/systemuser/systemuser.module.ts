import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SystemuserComponent } from './list/systemuser.component';
import { SystemuserDetailComponent } from './detail/systemuser-detail.component';
import { SystemuserUpdateComponent } from './update/systemuser-update.component';
import { SystemuserDeleteDialogComponent } from './delete/systemuser-delete-dialog.component';
import { SystemuserRoutingModule } from './route/systemuser-routing.module';

@NgModule({
  imports: [SharedModule, SystemuserRoutingModule],
  declarations: [SystemuserComponent, SystemuserDetailComponent, SystemuserUpdateComponent, SystemuserDeleteDialogComponent],
  entryComponents: [SystemuserDeleteDialogComponent],
})
export class SystemuserModule {}
