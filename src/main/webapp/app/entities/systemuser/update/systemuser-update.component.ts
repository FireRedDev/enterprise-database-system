import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISystemuser, Systemuser } from '../systemuser.model';
import { SystemuserService } from '../service/systemuser.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-systemuser-update',
  templateUrl: './systemuser-update.component.html',
})
export class SystemuserUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    entryDate: [],
    user: [null, Validators.required],
  });

  constructor(
    protected systemuserService: SystemuserService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemuser }) => {
      this.updateForm(systemuser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const systemuser = this.createFromForm();
    if (systemuser.id !== undefined) {
      this.subscribeToSaveResponse(this.systemuserService.update(systemuser));
    } else {
      this.subscribeToSaveResponse(this.systemuserService.create(systemuser));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISystemuser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(systemuser: ISystemuser): void {
    this.editForm.patchValue({
      id: systemuser.id,
      entryDate: systemuser.entryDate,
      user: systemuser.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, systemuser.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ISystemuser {
    return {
      ...new Systemuser(),
      id: this.editForm.get(['id'])!.value,
      entryDate: this.editForm.get(['entryDate'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
