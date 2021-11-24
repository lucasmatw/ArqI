package ar.edu.mercadogratis.app.service.batch;

import ar.edu.mercadogratis.app.batch.ProductFieldMapper;
import ar.edu.mercadogratis.app.batch.ProductItemProcessor;
import ar.edu.mercadogratis.app.model.Product;
import ar.edu.mercadogratis.app.model.User;
import ar.edu.mercadogratis.app.service.DateService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductBatchLoadService {

    private final DateService dateService;
    private final JobBuilderFactory jobFactory;
    private final StepBuilderFactory stepFactory;

    private final ProductDbWriterService productDbWriterService;

    private final JobLauncher jobLauncher;

    @SneakyThrows
    public BatchStatus loadCsvInput(User seller, InputStream inputStream) {

        Step step = stepFactory.get("product-file-load-step")
                .<Product, Product>chunk(2)
                .reader(buildProductItemReader(inputStream))
                .processor(buildItemProcessor(seller))
                .writer(productDbWriterService)
                .build();

        Job job = jobFactory.get("product-load-job")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();

        Map<String, JobParameter> params = new HashMap<>();

        Date nowDate = Date.from(dateService.getNowDate().atZone(ZoneId.systemDefault()).toInstant());
        params.put("time", new JobParameter(nowDate));
        JobParameters jobParams = new JobParameters(params);

        JobExecution run = jobLauncher.run(job, jobParams);

        return run.getStatus();
    }

    private ItemProcessor<Product, Product> buildItemProcessor(User seller) {
        return new ProductItemProcessor(seller);
    }


    public ItemReader<Product> buildProductItemReader(InputStream inputStream) {
        FlatFileItemReader<Product> productItemReader = new FlatFileItemReader<>();
        productItemReader.setName("productCsvReader");
        productItemReader.setResource(new InputStreamResource(inputStream));
        productItemReader.setLineMapper(buildLineMapper());

        return productItemReader;
    }

    private LineMapper<Product> buildLineMapper() {
        DefaultLineMapper<Product> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_COMMA);
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("name", "description", "category", "price", "stock");

        FieldSetMapper<Product> setMapper = new ProductFieldMapper();

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(setMapper);

        return defaultLineMapper;
    }
}
