import { TestBed } from '@angular/core/testing';

import { OnChangeComponentService } from './on-change-component.service';

describe('OnChangeComponentService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: OnChangeComponentService = TestBed.get(OnChangeComponentService);
    expect(service).toBeTruthy();
  });
});
