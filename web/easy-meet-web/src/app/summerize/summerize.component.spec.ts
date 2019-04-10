import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SummerizeComponent } from './summerize.component';

describe('SummerizeComponent', () => {
  let component: SummerizeComponent;
  let fixture: ComponentFixture<SummerizeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SummerizeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SummerizeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
