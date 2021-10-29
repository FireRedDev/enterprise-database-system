import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITargetSystem } from '../target-system.model';

@Component({
  selector: 'jhi-target-system-detail',
  templateUrl: './target-system-detail.component.html',
})
export class TargetSystemDetailComponent implements OnInit {
  targetSystem: ITargetSystem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ targetSystem }) => {
      this.targetSystem = targetSystem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
