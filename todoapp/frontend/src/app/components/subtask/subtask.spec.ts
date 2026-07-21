import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Subtask } from './subtask';

describe('Subtask', () => {
  let component: Subtask;
  let fixture: ComponentFixture<Subtask>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Subtask],
    }).compileComponents();

    fixture = TestBed.createComponent(Subtask);

    fixture.componentRef.setInput('todo', {
      todoId: 1,
      title: 'Test todo',
      completed: false,
    });

    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
