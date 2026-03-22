import { Component, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {Api, DirectoryAnalysisResult, FileAnalysisDetail, FileMetrics} from './services/api';
import { BaseChartDirective } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, BaseChartDirective],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  selectedFile: File | null = null;
  directoryPath: string = '';
  githubUrl: string = '';

  fileResult: FileMetrics | null = null;
  dirResult: DirectoryAnalysisResult | null = null;

  isLoading = false;
  errorMessage = '';

  constructor(private apiService: Api, private cdr: ChangeDetectorRef) {
  }

  public pieChartOptions: ChartOptions<'pie'> = {
    responsive: true,
    plugins: {
      legend: {position: 'bottom'},
      title: {display: true, text: 'Files complexity'}
    }
  };

  public pieChartData: ChartData<'pie'> = {
    labels: ['Healthy', 'Warning', 'Critical'], // healthy <= 10, warning (11-20), critical 21+
    datasets: [{data: [0, 0, 0]}]
  };

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  analyzeSingleFile(event?: Event) {
    if (event) event.preventDefault();
    if (!this.selectedFile) return;
    this.prepareForRequest();

    this.apiService.analyzeFile(this.selectedFile).subscribe({
      next: (result) => {
        this.fileResult = result;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => this.handleError(err)
    });
  }

  analyzeDirectory(event?: Event) {
    if (event) event.preventDefault();
    if (!this.directoryPath) return;
    this.prepareForRequest();

    this.apiService.analyzeDirectory(this.directoryPath).subscribe({
      next: (result) => {
        this.dirResult = result;
        this.isLoading = false;
        this.generateChartData(result.files);
        this.cdr.detectChanges();
      },
      error: (err) => this.handleError(err)
    });
  }

  analyzeGithub(event?: Event) {
    if (event) event.preventDefault();

    console.log('Sending request to:', this.githubUrl);
    if (!this.githubUrl) return;
    this.prepareForRequest();

    this.apiService.analyzeRepository(this.githubUrl).subscribe({
      next: (result) => {
        console.log('Data received:', result);
        this.dirResult = result;
        this.isLoading = false;
        this.generateChartData(result.files);
        this.cdr.detectChanges();
      },
      error: (err) => this.handleError(err)
    });
  }

  private prepareForRequest() {
    this.isLoading = true;
    this.errorMessage = '';
    this.fileResult = null;
    this.dirResult = null;
  }

  private handleError(err: any) {
    this.isLoading = false;
    this.errorMessage = 'Server communication error.';
    console.error(err);
    this.cdr.detectChanges();
  }

  private generateChartData(files: FileAnalysisDetail[]) {
    let healthy = 0;
    let warning = 0;
    let critical = 0;

    for (const file of files) {
      const complexity = file.fileMetrics.cyclomaticComplexity;
      if (complexity <= 10) {
        healthy++;
      } else if (complexity <= 20) {
        warning++;
      } else {
        critical++;
      }
    }
    this.pieChartData = {
      labels: ['Healthy (<11)', 'Warning (11-20)', 'Critical (>20)'],
      datasets: [{
        data: [healthy, warning, critical],
        backgroundColor: ['green', 'gold', 'red'],
        hoverBackgroundColor: ['darkgreen', 'darkgoldenrod', 'darkred']
      }]
    };
  }
}
