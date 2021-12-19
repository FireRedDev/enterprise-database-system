import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITargetsystem, TargetSystemTypes } from '../targetsystem.model';

@Component({
  selector: 'jhi-targetsystem-detail',
  templateUrl: './targetsystem-detail.component.html',
})
export class TargetsystemDetailComponent implements OnInit {
  targetsystem: ITargetsystem | null = null;
  systemtype = 'undefined';
  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ targetsystem }) => {
      this.targetsystem = targetsystem;
    });
    this.declare();
  }

  declare(): void {
    switch (this.targetsystem?.type) {
      case TargetSystemTypes.CSV: {
        this.systemtype = 'CSV';
        break;
      }
      case TargetSystemTypes.LDAV: {
        this.systemtype = 'LDAV';
        break;
      }
      case TargetSystemTypes.Database: {
        this.systemtype = 'Datenbank';
        break;
      }
    }
  }

  previousState(): void {
    window.history.back();
  }
}
