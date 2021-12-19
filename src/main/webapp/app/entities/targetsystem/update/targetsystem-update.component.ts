import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITargetsystem, Targetsystem, TargetSystemTypes } from '../targetsystem.model';
import { TargetsystemService } from '../service/targetsystem.service';

@Component({
  selector: 'jhi-targetsystem-update',
  templateUrl: './targetsystem-update.component.html',
})
export class TargetsystemUpdateComponent implements OnInit {
  isSaving = false;
  type = 'none';
  types = ['CSV', 'LDAB', 'Datenbank'];
  url = 'EMPTY';
  username = '';
  password = '';
  attributes = ['User ID', 'Username', 'Passwort', 'Systemuser', 'Targetsystem'];
  attributesFinal = this.attributes;
  attributesBoolean = [true, true, true, true, true];
  tryin: any;
  editForm = this.fb.group({
    id: [],
    name: [],
    type: [],
    dbUrl: [],
    dbuser: [],
    dbpassword: [],
    csvAttributes: [],
  });

  constructor(protected targetsystemService: TargetsystemService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ targetsystem }) => {
      this.updateForm(targetsystem);
    });
  }

  previousState(): void {
    window.history.back();
  }

  onCheckChange(): void {
    for (let i = 0; i < this.attributesBoolean.length; i++) {
      if (!this.attributesBoolean[i]) {
        this.attributesFinal[i] = '';
      } else {
        this.attributesFinal[i] = this.attributes[i];
      }
    }
  }

  save(): void {
    this.isSaving = true;
    const targetsystem = this.createFromForm();
    if (targetsystem.id !== undefined) {
      this.subscribeToSaveResponse(this.targetsystemService.update(targetsystem));
    } else {
      this.subscribeToSaveResponse(this.targetsystemService.create(targetsystem));
    }
  }

  selectType(id: any): void {
    if (id.value !== 'auswählen') {
      this.type = id.value;
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITargetsystem>>): void {
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

  protected updateForm(targetsystem: ITargetsystem): void {
    this.editForm.patchValue({
      id: targetsystem.id,
      name: targetsystem.name,
    });
  }

  protected createFromForm(): ITargetsystem {
    let finalType = TargetSystemTypes.CSV;
    switch (this.type) {
      case 'CSV': {
        finalType = TargetSystemTypes.CSV;
        break;
      }
      case 'LDAB': {
        finalType = TargetSystemTypes.LDAV;
        break;
      }
      case 'Datenbank': {
        finalType = TargetSystemTypes.Database;
        break;
      }
    }
    return {
      ...new Targetsystem(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: finalType,
      dbUrl: this.editForm.get('url')?.value,
      dbuser: this.editForm.get('username')?.value,
      dbpassword: this.editForm.get('password')?.value,
      csvAttributes: this.attributesBoolean,
    };
  }
}
