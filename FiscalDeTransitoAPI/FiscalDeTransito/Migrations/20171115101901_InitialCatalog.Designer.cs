﻿// <auto-generated />
using FiscalDeTransitoApi.Context;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage;
using Microsoft.EntityFrameworkCore.Storage.Internal;
using System;

namespace FiscalDeTransitoApi.Migrations
{
    [DbContext(typeof(ApplicationDbContext))]
    [Migration("20171115101901_InitialCatalog")]
    partial class InitialCatalog
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "2.0.0-rtm-26452")
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("FiscalDeTransitoApi.Models.Infracao", b =>
                {
                    b.Property<Guid>("Id")
                        .ValueGeneratedOnAdd();

                    b.Property<long>("IMEI");

                    b.Property<long>("Latitude");

                    b.Property<long>("Longitude");

                    b.HasKey("Id");

                    b.ToTable("Infracoes");
                });

            modelBuilder.Entity("FiscalDeTransitoApi.Models.InfracaoFile", b =>
                {
                    b.Property<Guid>("Id")
                        .ValueGeneratedOnAdd();

                    b.Property<byte[]>("File");

                    b.Property<Guid>("InfracaoId");

                    b.HasKey("Id");

                    b.HasIndex("InfracaoId")
                        .IsUnique();

                    b.ToTable("Files");
                });

            modelBuilder.Entity("FiscalDeTransitoApi.Models.InfracaoFile", b =>
                {
                    b.HasOne("FiscalDeTransitoApi.Models.Infracao", "Infracao")
                        .WithOne("File")
                        .HasForeignKey("FiscalDeTransitoApi.Models.InfracaoFile", "InfracaoId")
                        .OnDelete(DeleteBehavior.Cascade);
                });
#pragma warning restore 612, 618
        }
    }
}
